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

export interface PlanResponse {
    code: string;
    name: string;
    description: string;
    amount: number;
    currency: string;
    billingInterval: string;
}