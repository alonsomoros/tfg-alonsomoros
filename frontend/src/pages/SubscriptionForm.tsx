import { useEffect, useState, type ChangeEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import type { PlanResponse, SubscriptionPayload } from "../features/types";
import { createSubscription } from '../api/subscriptionServiceApi';
import { StripePaymentForm } from '../components/payment/StripePaymentForm';
import './SubscriptionForm.css';

export function SubscriptionForm() {
    const [plan, setPlan] = useState<PlanResponse | null>(null);
    const navigate = useNavigate();

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
            setError('Error while processing the subscription.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <main className="payment-page app-page">
            <div className="payment-card">
                <header className="payment-card__header">
                   <div className="payment-card__header-left">
                        <button className="payment-card__close" type="button" aria-label="Cerrar formulario" onClick={() => navigate('/')}>
                            X
                        </button>
                        <h1 className="payment-card__title">Details of the subscription</h1>
                    </div>

                    <div className="payment-card__total">
                        <strong>{plan ? `${plan.amount.toFixed(2)} ${plan.currency}` : '0.00'}</strong>
                    </div>
                </header>

                {error && <div style={{ color: 'red', marginBottom: '15px' }}>{error}</div>}

                
                <section className="payment-method-selector">
                   <h2 className="payment-section-title">Payment Method:</h2>
                    <div className="payment-methods">
                        <div 
                            className={`payment-method-btn ${paymentMethod === 'card' ? 'active' : ''}`}
                            onClick={() => setPaymentMethod('card')}
                        >
                            <span className="icon">💳</span>
                            <span>Payment with card</span>
                        </div>
                        <div 
                            className={`payment-method-btn ${paymentMethod === 'paypal' ? 'active' : ''}`}
                            onClick={() => setPaymentMethod('paypal')}
                        >
                            <span className="icon">🅿️</span>
                            <span>Payment with PayPal</span>
                        </div>
                        <div 
                            className={`payment-method-btn ${paymentMethod === 'others' ? 'active' : ''}`}
                            onClick={() => setPaymentMethod('others')}
                        >
                            <span className="icon">🌐</span>
                            <span>Others</span>
                        </div>
                    </div>
                </section>

                <div className="payment-form">
                    <label className="payment-field">
                        <span>Email</span>
                        <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                    </label>
                    <label className="payment-field">
                        <span>Cardholder Name (Optional)</span>
                        <input type="text" name="cardHolder" value={formData.cardHolder} onChange={handleChange} />
                    </label>

                    <label className="payment-terms" style={{ marginTop: '20px' }}>
                        <input type="checkbox" checked={acceptTerms} onChange={(e) => setAcceptTerms(e.target.checked)} />
                        <span>I have read and accept the terms and conditions.</span>
                    </label>
                </div>

                <div style={{ marginTop: '20px' }}>
                    {paymentMethod === 'card' && (
                        <StripePaymentForm 
                            // Le pasamos el callback y validamos que pueda enviar
                            onSuccess={(token) => handlePaymentSuccess('STRIPE', token)} 
                            disabled={!acceptTerms || !formData.email || isSubmitting}
                            isSubmitting={isSubmitting}
                        />
                    )}

                    {paymentMethod === 'paypal' && (
                        <div className="payment-placeholder">
                            <p>You will be redirected to PayPal (Coming soon...)</p>
                        </div>
                    )}
                </div>
            </div>
        </main>
    );
}