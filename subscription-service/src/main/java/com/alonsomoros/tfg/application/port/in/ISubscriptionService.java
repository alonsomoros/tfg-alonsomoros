package com.alonsomoros.tfg.application.port.in;

import java.time.LocalDate;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;

public interface ISubscriptionService {
    SubscriptionResponseDto createSubscription(CreateSubscriptionCommand createSubscriptionCommand);

    void updatePaymentDate(Long subscriptionId, LocalDate newPaymentDate);
}
