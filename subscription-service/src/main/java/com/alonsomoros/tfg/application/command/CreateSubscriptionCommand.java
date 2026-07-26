package com.alonsomoros.tfg.application.command;

public record CreateSubscriptionCommand(
    String customerEmail,
    String planId,
    PaymentDetailsCommand paymentInfo
) {}
