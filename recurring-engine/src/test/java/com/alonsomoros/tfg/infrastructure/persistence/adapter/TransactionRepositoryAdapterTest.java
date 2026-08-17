package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.application.exception.PaymentMethodNotFoundException;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.model.Transaction;
import com.alonsomoros.tfg.domain.model.TransactionStatusEnum;
import com.alonsomoros.tfg.infrastructure.mapper.TransactionMapper;
import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMethodEntity;
import com.alonsomoros.tfg.infrastructure.persistence.entity.TransactionEntity;
import com.alonsomoros.tfg.infrastructure.persistence.repository.PaymentMethodRepository;
import com.alonsomoros.tfg.infrastructure.persistence.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryAdapterTest {

    @Mock
    private TransactionRepository jpaRepository;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private TransactionRepositoryAdapter adapter;

    @Test
    void save_linksPaymentMethodAndPersists() {
        UUID paymentMethodId = UUID.randomUUID();
        UUID savedTransactionId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("19.99");

        PaymentMethod paymentMethod = PaymentMethod.builder().id(paymentMethodId).build();
        Transaction domain = Transaction.builder()
                .paymentMethod(paymentMethod)
                .amount(amount)
                .status(TransactionStatusEnum.SUCCESS)
                .build();

        PaymentMethodEntity paymentMethodEntity = PaymentMethodEntity.builder().id(paymentMethodId).build();
        TransactionEntity mappedEntity = TransactionEntity.builder()
                .amount(amount)
                .status(TransactionStatusEnum.SUCCESS)
                .build();
        TransactionEntity savedEntity = TransactionEntity.builder()
                .transactionId(savedTransactionId)
                .paymentMethodEntity(paymentMethodEntity)
                .amount(amount)
                .status(TransactionStatusEnum.SUCCESS)
                .build();
        Transaction savedDomain = Transaction.builder()
                .id(savedTransactionId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .status(TransactionStatusEnum.SUCCESS)
                .build();

        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(paymentMethodEntity));
        when(mapper.toEntity(domain)).thenReturn(mappedEntity);
        when(jpaRepository.save(any(TransactionEntity.class))).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedDomain);

        Transaction result = adapter.save(domain);

        ArgumentCaptor<TransactionEntity> captor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getPaymentMethodEntity()).isEqualTo(paymentMethodEntity);
        assertThat(result).isEqualTo(savedDomain);
    }

    @Test
    void save_whenPaymentMethodMissing_throws() {
        UUID paymentMethodId = UUID.randomUUID();
        Transaction domain = Transaction.builder()
                .paymentMethod(PaymentMethod.builder().id(paymentMethodId).build())
                .amount(new BigDecimal("5.00"))
                .status(TransactionStatusEnum.FAILED)
                .build();

        when(paymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.save(domain))
                .isInstanceOf(PaymentMethodNotFoundException.class)
                .hasMessageContaining(paymentMethodId.toString());
    }
}
