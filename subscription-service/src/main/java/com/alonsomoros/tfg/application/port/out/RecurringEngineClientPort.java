package com.alonsomoros.tfg.application.port.out;

import com.alonsomoros.tfg.application.command.PaymentDetailsCommand;

public interface RecurringEngineClientPort {
    void sendPaymentToken(Long subscriptionId, PaymentDetailsCommand paymentInfoCommand);
}
