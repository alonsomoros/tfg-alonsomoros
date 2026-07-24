import { useState } from 'react';
import { createSubscription } from '../api/subscriptionServiceApi';
import type { SubscriptionPayload } from '../features/types';

export function SubscriptionForm() {
    const [formData, setFormData] = useState({
        cardHolder: '',
        cardNumber: '',
        expiryDate: '',
        cvv: '',
        email: '',
    });

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');

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

        setIsSubmitting(true);

        try {
            // Simulate a 1.5 second delay for the payment gateway call.
            await new Promise(resolve => setTimeout(resolve, 1500));
            
            const mockToken = `tok_mock_${Math.random().toString(36).substring(2, 10)}`;
            
            const last4Digits = formData.cardNumber.slice(-4);
            const [expYear, expMonth] = formData.expiryDate.split('-');
            console.log('Form data:', formData);

            const payloadToBackend: SubscriptionPayload = {
                customerEmail: formData.email,
                planId: "PREMIUM_MENSUAL",
                paymentInfo: {
                    provider: "MOCK",
                    token: mockToken,
                    cardHolder: formData.cardHolder,
                    expiryMonth: expMonth || "12",
                    expiryYear: expYear || "26",
                    last4: last4Digits
                }
            };

            console.log('Payload for the Subscription Service backend:', payloadToBackend);
            
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
        <main className="payment-page">
            <form className="payment-card" onSubmit={handleSubmit}>
                <header className="payment-card__header">
                    <button className="payment-card__close" type="button" aria-label="Cerrar formulario">
                        X
                    </button>

                    <div className="payment-card__total">
                        <span className="payment-card__label">Total</span>
                        <strong>100,00€</strong>
                    </div>
                </header>

                <p className="payment-card__note">(*) Completa los datos para autorizar el pago recurrente.</p>
                {error && <div style={{ color: 'red', marginBottom: '10px' }}>{error}</div>}

                <div className="payment-form">
                    <label className="payment-field">
                        <span>Titular de la Tarjeta</span>
                        <input type="text" name="cardHolder" placeholder="Titular de la Tarjeta" value={formData.cardHolder} onChange={handleChange} required/>
                    </label>

                    <label className="payment-field">
                        <span>Número de la Tarjeta</span>
                        <input type="text" name="cardNumber" placeholder="Número de la Tarjeta" inputMode="numeric" value={formData.cardNumber} onChange={handleChange} required/>
                    </label>

                    <div className="payment-form__split">
                        <label className="payment-field">
                            <span>Fecha Caducidad</span>
                            <input type="date" min={new Date().toISOString().split('T')[0]} name="expiryDate" placeholder="Fecha Caducidad" inputMode="numeric" value={formData.expiryDate} onChange={handleChange} required/>
                        </label>

                        <label className="payment-field">
                            <span>CVV</span>
                            <input type="password" name="cvv" placeholder="CVV" inputMode="numeric" value={formData.cvv} onChange={handleChange} required/>
                        </label>
                    </div>

                    <label className="payment-field">
                        <span>Correo Electrónico</span>
                        <input type="email" name="email" placeholder="Correo Electrónico" value={formData.email} onChange={handleChange} required/>
                    </label>

                    <button className="payment-card__submit" type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Procesando...' : 'Pagar'}
                    </button>
                </div>
            </form>
        </main>
    );
}