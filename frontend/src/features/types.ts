export interface PaymentInfo {
    provider: string;
    token: string;
    cardHolder: string;
    expiryMonth: string;
    expiryYear: string;
    last4: string;
}

export interface SubscriptionPayload {
    customerEmail: string;
    planId: string;
    paymentInfo: PaymentInfo;
}