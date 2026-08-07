package com.alonsomoros.tfg.infrastructure.web.dto.request;

import java.time.LocalDate;

public record UpdatePaymentDateRequestDto(
        LocalDate newPaymentDate
) {
    
}
