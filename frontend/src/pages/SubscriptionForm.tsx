import { useEffect, useState, type ChangeEvent, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import type { PlanResponse, SubscriptionPayload } from "../features/types";
import { createSubscription } from '../api/subscriptionServiceApi';
import './SubscriptionForm.css';

export function SubscriptionForm() {

    const [plan, setPlan] = useState<PlanResponse | null>(null);
    const navigate = useNavigate();

    // Estado para el método de pago seleccionado
    const [paymentMethod, setPaymentMethod] = useState<'card' | 'paypal' | 'others'>('card');
    // Estado para los términos y condiciones
    const [acceptTerms, setAcceptTerms] = useState(false);

    const [formData, setFormData] = useState({
        cardHolder: '',
        cardNumber: '',
        expiryDate: '',
        cvv: '',
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
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');

        if (!acceptTerms) {
            setError('Debes aceptar los términos y condiciones para continuar.');
            return;
        }

        if (!plan) {
            setError('Error: No se ha seleccionado ningún plan.');
            return;
        }

        // Validaciones específicas si es tarjeta
        if (paymentMethod === 'card') {
            if (!formData.cardNumber || formData.cardNumber.length < 16) {
                setError('El número de tarjeta debe tener al menos 16 dígitos.');
                return;
            }

            if (!formData.cvv || formData.cvv.length < 3 || isNaN(Number(formData.cvv))) {
                setError('El CVV debe tener al menos 3 dígitos y ser numérico.');
                return;
            }

            if (!formData.email.includes('@')) {
                setError('Introduce un correo válido.');
                return;
            }
        }

        setIsSubmitting(true);

        try {
            await new Promise(resolve => setTimeout(resolve, 1500));

            // Si es paypal u otros, aquí iría la lógica de esos proveedores
            // Simulamos el flujo actual para tarjeta:
            const mockToken = `tok_mock_${Math.random().toString(36).substring(2, 10)}`;
            const last4Digits = paymentMethod === 'card' ? formData.cardNumber.slice(-4) : '0000';
            const [expYear, expMonth] = formData.expiryDate.split('-');
            
            const payloadToBackend: SubscriptionPayload = {
                customerEmail: formData.email || 'user@example.com',
                planId: plan.code,
                paymentInfo: {
                    provider: paymentMethod.toUpperCase(),
                    token: mockToken,
                    cardHolder: formData.cardHolder || 'N/A',
                    expiryMonth: expMonth || "12",
                    expiryYear: expYear || "26",
                    last4: last4Digits
                }
            };

            const response = await createSubscription(payloadToBackend);
            console.log('Subscription Service backend response:', response);
            alert('Simulation successful. Check the console.');
            
        } catch (err) {
            setError('There was an error processing the payment.');
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <main className="payment-page app-page">
            <form className="payment-card" onSubmit={handleSubmit}>
                
                {/* NUEVA CABECERA: Botón X + Título + Total */}
                <header className="payment-card__header">
                    <div className="payment-card__header-left">
                        <button className="payment-card__close" type="button" aria-label="Cerrar formulario" onClick={() => navigate('/')}>
                            X
                        </button>
                        <h1 className="payment-card__title">Detalles del pago/suscripción</h1>
                    </div>

                    <div className="payment-card__total">
                        <strong>{plan ? `${plan.amount.toFixed(2)} ${plan.currency}` : '0.00'}</strong>
                    </div>
                </header>

                {error && <div style={{ color: 'red', marginBottom: '15px' }}>{error}</div>}

                {/* SELECTOR DE MÉTODOS DE PAGO */}
                <section className="payment-method-selector">
                    <h2 className="payment-section-title">Método de Pago:</h2>
                    <div className="payment-methods">
                        <div 
                            className={`payment-method-btn ${paymentMethod === 'card' ? 'active' : ''}`}
                            onClick={() => setPaymentMethod('card')}
                        >
                            <span className="icon">💳</span>
                            <span>Pago con tarjeta</span>
                        </div>
                        <div 
                            className={`payment-method-btn ${paymentMethod === 'paypal' ? 'active' : ''}`}
                            onClick={() => setPaymentMethod('paypal')}
                        >
                            <span className="icon">🅿️</span>
                            <span>Pago con PayPal</span>
                        </div>
                        <div 
                            className={`payment-method-btn ${paymentMethod === 'others' ? 'active' : ''}`}
                            onClick={() => setPaymentMethod('others')}
                        >
                            <span className="icon">🌐</span>
                            <span>Otros</span>
                        </div>
                    </div>
                </section>

                {/* FORMULARIO DINÁMICO */}
                {paymentMethod === 'card' && (
                    <div className="payment-form">
                        <label className="payment-field">
                            <span>Titular de la Tarjeta</span>
                            <input type="text" name="cardHolder" placeholder="Titular de la Tarjeta" value={formData.cardHolder} onChange={handleChange} required />
                        </label>

                        <label className="payment-field">
                            <span>Número de la Tarjeta</span>
                            <input type="text" name="cardNumber" placeholder="Número de la Tarjeta" inputMode="numeric" value={formData.cardNumber} onChange={handleChange} required />
                        </label>

                        <div className="payment-form__split">
                            <label className="payment-field">
                                <span>Fecha Caducidad</span>
                                <input type="date" min={new Date().toISOString().split('T')[0]} name="expiryDate" value={formData.expiryDate} onChange={handleChange} required />
                            </label>

                            <label className="payment-field">
                                <span>CVV</span>
                                <input type="password" name="cvv" placeholder="CVV" inputMode="numeric" value={formData.cvv} onChange={handleChange} required />
                            </label>
                        </div>

                        <label className="payment-field">
                            <span>Correo Electrónico</span>
                            <input type="email" name="email" placeholder="Correo Electrónico" value={formData.email} onChange={handleChange} required />
                        </label>
                    </div>
                )}

                {paymentMethod === 'paypal' && (
                    <div className="payment-placeholder">
                        <p>Serás redirigido a PayPal para completar tu compra de forma segura.</p>
                    </div>
                )}

                {paymentMethod === 'others' && (
                    <div className="payment-placeholder">
                        <p>Opciones de pago alternativas (Apple Pay, Google Pay, etc.) irían aquí.</p>
                    </div>
                )}

                {/* TÉRMINOS Y CONDICIONES */}
                <label className="payment-terms">
                    <input 
                        type="checkbox" 
                        checked={acceptTerms} 
                        onChange={(e) => setAcceptTerms(e.target.checked)} 
                        required 
                    />
                    <span>He leído y acepto los términos y condiciones.</span>
                </label>

                {/* BOTÓN CONFIRMAR CON TEXTO DINÁMICO */}
                <button className="payment-card__submit" type="submit" disabled={isSubmitting}>
                    {isSubmitting 
                        ? 'Procesando...' 
                        : plan 
                            ? `Confirmar suscripción - ${plan.amount.toFixed(2)}${plan.currency}/${plan.billingInterval.toLowerCase()}`
                            : 'Confirmar suscripción'
                    }
                </button>
            </form>
        </main>
    );
}