import React, { useState } from 'react';
import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";

interface PayPalFormProps {
    onSuccess: (token: string) => void;
    disabled: boolean;
    isSubmitting: boolean;
}

export const PayPalPaymentForm: React.FC<PayPalFormProps> = ({ onSuccess, disabled, isSubmitting }) => {
    const [paypalError, setPaypalError] = useState<string | null>(null);
    const initialOptions = {
        clientId: import.meta.env.VITE_PAYPAL_CLIENT_ID || "test",
        currency: "EUR",
        intent: "capture" 
    };

    return (
        <div style={{ marginTop: '20px' }}>
            <PayPalScriptProvider options={initialOptions}>
                {paypalError && <div style={{ color: 'red', marginBottom: '10px' }}>{paypalError}</div>}
                
                <div style={{ opacity: disabled || isSubmitting ? 0.5 : 1, pointerEvents: disabled || isSubmitting ? 'none' : 'auto' }}>
                    <PayPalButtons 
                        style={{ layout: "vertical", color: "gold", shape: "rect", label: "paypal" }}
                        
                        createOrder={(data, actions) => {
                            return actions.order.create({
                                intent: "CAPTURE",
                                purchase_units: [
                                    {
                                        description: "Subscription - TFG",
                                        amount: {
                                            currency_code: "EUR",
                                            value: "0.10",
                                        },
                                    },
                                ],
                            });
                        }}
                        
                        onApprove={async (data, actions) => {
                            console.log("¡Paypal authorized! ID:", data.orderID);
                            if (data.orderID) {
                                onSuccess(data.orderID);
                            } else {
                                setPaypalError("Could not retrieve order ID from PayPal.");
                            }
                        }}
                        
                        onCancel={() => {
                            console.log("User closed the Paypal popup or canceled the payment.");
                        }}
                        
                        onError={(err) => {
                            console.error("Paypal error:", err);
                            setPaypalError("Could not load PayPal.");
                        }}
                    />
                </div>
            </PayPalScriptProvider>
        </div>
    );
};