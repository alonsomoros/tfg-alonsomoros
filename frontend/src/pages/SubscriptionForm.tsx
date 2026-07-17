export function SubscriptionForm() {
    return (
        <main className="payment-page">
            <form className="payment-card">
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

                <div className="payment-form">
                    <label className="payment-field">
                        <span>Titular de la Tarjeta</span>
                        <input type="text" name="cardHolder" placeholder="Titular de la Tarjeta" />
                    </label>

                    <label className="payment-field">
                        <span>Número de la Tarjeta</span>
                        <input type="text" name="cardNumber" placeholder="Número de la Tarjeta" inputMode="numeric" />
                    </label>

                    <div className="payment-form__split">
                        <label className="payment-field">
                            <span>Fecha Caducidad</span>
                            <input type="text" name="expiryDate" placeholder="Fecha Caducidad" inputMode="numeric" />
                        </label>

                        <label className="payment-field">
                            <span>CVV</span>
                            <input type="password" name="cvv" placeholder="CVV" inputMode="numeric" />
                        </label>
                    </div>

                    <label className="payment-field">
                        <span>Correo Electrónico</span>
                        <input type="email" name="email" placeholder="Correo Elec" />
                    </label>

                    <button className="payment-card__submit" type="submit">
                        Pagar
                    </button>
                </div>
            </form>
        </main>
    );
}