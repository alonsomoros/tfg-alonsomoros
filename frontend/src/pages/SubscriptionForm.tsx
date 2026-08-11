import { useEffect, useState, type ChangeEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { createSubscription } from '../api/subscriptionServiceApi';
import { PayPalPaymentForm } from '../components/payment/PayPalPaymentForm';
import { StripePaymentForm, } from '../components/payment/StripePaymentForm';
import type { PlanResponse, SubscriptionPayload } from "../features/types";
import './SubscriptionForm.css';

function getErrorMessage(error: unknown, fallbackMessage: string): string {
    if (error instanceof Error && error.message) {
        return error.message;
    }
    return fallbackMessage;
}

export function SubscriptionForm() {
    const [plan, setPlan] = useState<PlanResponse | null>(null);
    const navigate = useNavigate();

    const [step, setStep] = useState<1 | 2>(1);

    const [paymentMethod, setPaymentMethod] = useState<'card' | 'paypal' | 'others'>('card');
    const [acceptTerms, setAcceptTerms] = useState(false);

    const [formData, setFormData] = useState({
        cardHolder: '',
        email: '',
    });

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const savedPlan = localStorage.getItem('selectedPlan');
        if (savedPlan) {
            setPlan(JSON.parse(savedPlan));
        } else {
            navigate('/');
        }
    }, [navigate]);

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    const handleNextStep = () => {
        if (!formData.email || !formData.email.includes('@')) {
            setError('Por favor, introduce un correo electrónico válido.');
            return;
        }
        setError('');
        setStep(2); // Avanzamos al pago
    };

    const handlePaymentSuccess = async (provider: string, token: string) => {
        setError('');
        setIsSubmitting(true);

        try {
            // Stripe Element oculta los datos por seguridad PCI, de ahí los Dummies
            const payloadToBackend: SubscriptionPayload = {
                customerEmail: formData.email,
                planId: plan!.code,
                paymentInfo: {
                    provider: provider.toUpperCase(),
                    token: token,
                    cardHolder: formData.cardHolder || 'N/A',
                    expiryMonth: "12", // Dummy temporal
                    expiryYear: "2030", // Dummy temporal
                    last4: "0000"       // Dummy temporal
                }
            };

            const response = await createSubscription(payloadToBackend);
            console.log('Subscription Service backend response:', response);
            alert('¡Subscription and payment configured successfully!');
            navigate('/');
            
        } catch (err) {
            setError(getErrorMessage(err, 'Error while processing the subscription.'));
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <main className="payment-page app-page">
            <div className="payment-card">
                <header className="payment-card__header">
                    <div className="payment-card__header-left">
                        <button className="payment-card__close" type="button" onClick={() => navigate('/')}>X</button>
                        <h1 className="payment-card__title">
                            {step === 1 ? 'Tus Datos' : 'Método de Pago'}
                        </h1>
                    </div>
                    <div className="payment-card__total">
                        <strong>{plan ? `${plan.amount.toFixed(2)} ${plan.currency}` : '0.00'}</strong>
                    </div>
                </header>

                {error && <div style={{ color: 'red', marginBottom: '15px' }}>{error}</div>}

                {step === 1 && (
                    <div className="payment-form">
                        <label className="payment-field">
                            <span>Correo Electrónico *</span>
                            <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                        </label>
                        <label className="payment-field">
                            <span>Titular de la Tarjeta / Cuenta (Opcional)</span>
                            <input type="text" name="cardHolder" value={formData.cardHolder} onChange={handleChange} />
                        </label>

                        <button 
                            className="payment-card__submit" 
                            style={{ marginTop: '20px' }}
                            type="button" 
                            onClick={handleNextStep}
                        >
                            Continuar al pago
                        </button>
                    </div>
                )}

                {step === 2 && (
                    <>
                        <div style={{ marginBottom: '15px', fontSize: '0.9rem', color: '#666' }}>
                            Comprando como: <strong>{formData.email}</strong> 
                            <button 
                                onClick={() => setStep(1)} 
                                style={{ background: 'none', border: 'none', color: '#007bff', cursor: 'pointer', marginLeft: '10px', textDecoration: 'underline' }}
                            >
                                Cambiar
                            </button>
                        </div>

                        <section className="payment-method-selector">
                             <div className="payment-methods">
                                <div className={`payment-method-btn ${paymentMethod === 'card' ? 'active' : ''}`} onClick={() => setPaymentMethod('card')}>
                                    <span className="icon">💳</span><span>Tarjeta</span>
                                </div>
                                <div className={`payment-method-btn ${paymentMethod === 'paypal' ? 'active' : ''}`} onClick={() => setPaymentMethod('paypal')}>
                                    <span className="icon">🅿️</span><span>PayPal</span>
                                </div>
                            </div>
                        </section>

                        <label className="payment-terms" style={{ marginTop: '20px', display: 'block' }}>
                            <input type="checkbox" checked={acceptTerms} onChange={(e) => setAcceptTerms(e.target.checked)} />
                            <span> He leído y acepto los términos y condiciones.</span>
                        </label>

                        <div style={{ marginTop: '20px' }}>
                            {paymentMethod === 'card' && (
                                <StripePaymentForm 
                                    customerEmail={formData.email}
                                    onSuccess={(token) => handlePaymentSuccess('STRIPE', token)} 
                                    disabled={!acceptTerms || isSubmitting}
                                    isSubmitting={isSubmitting}
                                />
                            )}

                            {paymentMethod === 'paypal' && (
                                <PayPalPaymentForm 
                                    onSuccess={(token) => handlePaymentSuccess('PAYPAL', token)} 
                                    disabled={!acceptTerms || isSubmitting}
                                    isSubmitting={isSubmitting}
                                />
                            )}
                        </div>
                    </>
                )}
            </div>
        </main>
    );
}