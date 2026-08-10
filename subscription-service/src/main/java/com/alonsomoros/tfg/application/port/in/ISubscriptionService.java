package com.alonsomoros.tfg.application.port.in;

import java.time.LocalDate;
import java.util.UUID;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;

public interface ISubscriptionService {
    SubscriptionResponseDto createSubscription(CreateSubscriptionCommand createSubscriptionCommand);

    void updatePaymentDate(UUID subscriptionId, LocalDate newPaymentDate);
}
