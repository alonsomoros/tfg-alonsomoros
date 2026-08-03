import React, { useState, useEffect } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { Elements, PaymentElement, useStripe, useElements } from '@stripe/react-stripe-js';
import { createSetupIntent } from '../../api/recurringEngineApi';

const stripePublicKey = import.meta.env.VITE_STRIPE_PUBLIC_KEY;

if (!stripePublicKey) {
    console.error("Public Key is missing. Check .env file.");
}

const stripePromise = loadStripe(stripePublicKey);

interface CheckoutFormProps {
    onSuccess: (token: string) => void;
    disabled: boolean;
    isSubmitting: boolean;
}

const CheckoutForm: React.FC<CheckoutFormProps> = ({ onSuccess, disabled, isSubmitting }) => {
    const stripe = useStripe();
    const elements = useElements();
    const [stripeError, setStripeError] = useState<string | null>(null);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        if (!stripe || !elements || disabled) return;

        const { error, setupIntent } = await stripe.confirmSetup({
            elements,
            redirect: 'if_required', // Evita redirecciones si no hay 3D Secure
        });

        if (error) {
            setStripeError(error.message || 'Error validating the card');
        } else if (setupIntent && setupIntent.status === 'succeeded') {
            setStripeError(null);
            onSuccess(setupIntent.payment_method as string);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <PaymentElement options={{ layout: 'tabs', wallets: { applePay: 'never', googlePay: 'never' } }} />
            
            {stripeError && <div style={{ color: 'red', marginTop: '10px' }}>{stripeError}</div>}
            
            <button 
                className="payment-card__submit" 
                style={{ marginTop: '20px' }}
                type="submit" 
                disabled={disabled || !stripe || isSubmitting}
            >
                {isSubmitting ? 'Procesando pago seguro...' : 'Confirmar suscripción'}
            </button>
        </form>
    );
};

interface StripeFormProps {
    onSuccess: (token: string) => void;
    disabled: boolean;
    isSubmitting: boolean;
}

export const StripePaymentForm: React.FC<StripeFormProps> = ({ onSuccess, disabled, isSubmitting }) => {
    const [clientSecret, setClientSecret] = useState("");
    const [connectionError, setConnectionError] = useState<string | null>(null);

    useEffect(() => {
        const fetchSecret = async () => {
            try {
                const data = await createSetupIntent();
                setClientSecret(data.clientSecret);
                console.log("The secret passed to Stripe is exactly:", clientSecret);
            } catch (err) {
                setConnectionError("Failed to connect to the secure payment gateway.");
            }
        };

        fetchSecret();
    }, []);

    if (connectionError) {
        return <div style={{ color: 'red', padding: '20px', border: '1px solid red' }}>{connectionError}</div>;
    }

    if (!clientSecret) {
        return <div className="payment-placeholder">Connecting to the secure payment gateway...</div>;
    }

    return (
        <Elements stripe={stripePromise} options={{ clientSecret }}>
            <CheckoutForm onSuccess={onSuccess} disabled={disabled} isSubmitting={isSubmitting} />
        </Elements>
    );
};