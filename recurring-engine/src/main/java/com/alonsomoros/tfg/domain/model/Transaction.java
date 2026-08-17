package com.alonsomoros.tfg.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @NotNull
    @Schema(description = "Identificador único de la transacción", example = "9ad19705-dcbe-448c-a7b2-d625e45c05d7")
    private UUID id;

    @NotNull
    @Schema(description = "Método de pago asociado a la transacción")
    private PaymentMethod paymentMethod;

    @NotNull
    @Schema(description = "Cantidad de la transacción", example = "100.00")
    private BigDecimal amount;

    @NotNull
    @Schema(description = "Estado de la transacción", example = "SUCCESS")
    private TransactionStatusEnum status;

}
