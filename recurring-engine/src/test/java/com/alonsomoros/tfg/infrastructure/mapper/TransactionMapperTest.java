package com.alonsomoros.tfg.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.model.Transaction;
import com.alonsomoros.tfg.domain.model.TransactionStatusEnum;
import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMethodEntity;
import com.alonsomoros.tfg.infrastructure.persistence.entity.TransactionEntity;

@ExtendWith(MockitoExtension.class)
class TransactionMapperTest {

    @Mock
    private PaymentMethodMapper paymentMethodMapper;

    @InjectMocks
    private TransactionMapper transactionMapper;

    @Test
    void toDomain_mapsAllFields() {
        UUID transactionId = UUID.randomUUID();
        UUID paymentMethodId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("25.50");

        PaymentMethodEntity paymentMethodEntity = PaymentMethodEntity.builder()
                .id(paymentMethodId)
                .build();
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .id(paymentMethodId)
                .build();

        TransactionEntity entity = TransactionEntity.builder()
                .transactionId(transactionId)
                .paymentMethodEntity(paymentMethodEntity)
                .amount(amount)
                .status(TransactionStatusEnum.SUCCESS)
                .build();

        when(paymentMethodMapper.toDomain(paymentMethodEntity)).thenReturn(paymentMethod);

        Transaction domain = transactionMapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(transactionId);
        assertThat(domain.getPaymentMethod()).isEqualTo(paymentMethod);
        assertThat(domain.getAmount()).isEqualByComparingTo(amount);
        assertThat(domain.getStatus()).isEqualTo(TransactionStatusEnum.SUCCESS);
    }

    @Test
    void toDomain_whenNull_returnsNull() {
        assertThat(transactionMapper.toDomain(null)).isNull();
    }

    @Test
    void toEntity_mapsAmountAndStatusWithoutPaymentMethod() {
        UUID transactionId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("10.00");

        Transaction domain = Transaction.builder()
                .id(transactionId)
                .amount(amount)
                .status(TransactionStatusEnum.FAILED)
                .build();

        TransactionEntity entity = transactionMapper.toEntity(domain);

        assertThat(entity.getTransactionId()).isEqualTo(transactionId);
        assertThat(entity.getAmount()).isEqualByComparingTo(amount);
        assertThat(entity.getStatus()).isEqualTo(TransactionStatusEnum.FAILED);
        assertThat(entity.getPaymentMethodEntity()).isNull();
    }

    @Test
    void toEntity_whenNull_returnsNull() {
        assertThat(transactionMapper.toEntity(null)).isNull();
    }
}
