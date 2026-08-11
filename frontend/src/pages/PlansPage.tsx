import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getPlans } from "../api/subscriptionServiceApi";
import type { PlanResponse } from "../features/types";
import "./PlansPage.css";

function getErrorMessage(error: unknown, fallbackMessage: string): string {
  if (error instanceof Error && error.message) {
    return error.message;
  }
  return fallbackMessage;
}

const currencyFormatter = new Intl.NumberFormat("es-ES", {
  style: "currency",
  currency: "EUR",
});

function formatBillingInterval(interval: string) {
  return interval.toLowerCase().replaceAll("_", " ");
}

export function PlansPage() {
  const navigate = useNavigate();
  const [plans, setPlans] = useState<PlanResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");


  const handleSelectPlan = (plan: PlanResponse) => {
    localStorage.setItem('selectedPlan', JSON.stringify(plan));
    navigate('/subscription-form');
  };

  useEffect(() => {
    document.title = "Subscription Plans";

    const loadPlans = async () => {
      try {
        setLoading(true);
        setError("");
        const response = await getPlans();
        setPlans(response);
      } catch (loadError) {
        setError(getErrorMessage(loadError, "Could not load available plans."));
      } finally {
        setLoading(false);
      }
    };

    void loadPlans();
  }, []);

  return (
    <main className="plans-page app-page">
      <section className="plans-page__hero app-shell">
        <span className="section-eyebrow">Subscription options</span>
        <h1 className="section-title">
          Choose the plan that fits your business model
        </h1>
        <p className="section-description">
          These plans are loaded directly from the backend and presented as
          clean, reusable cards so the UI can scale with future billing
          offerings.
        </p>
      </section>

      <section className="plans-page__content app-shell">
        {loading && <p className="plans-page__status">Loading plans...</p>}
        {error && (
          <p className="plans-page__status plans-page__status--error">
            {error}
          </p>
        )}

        {!loading && !error && (
          <div className="plans-grid">
            {plans.map((plan) => (
              <article className="plan-card" key={plan.code}>
                <div className="plan-card__top">
                  <div>
                    <span className="plan-card__badge">
                      {formatBillingInterval(plan.billingInterval)}
                    </span>
                    <h2>{plan.name}</h2>
                  </div>
                  <div className="plan-card__price">
                    <strong>{currencyFormatter.format(plan.amount)}</strong>
                    <span>/{formatBillingInterval(plan.billingInterval)}</span>
                  </div>
                </div>

                <p className="plan-card__description">{plan.description}</p>

                <div className="plan-card__meta">
                  <span>Code</span>
                  <strong>{plan.code}</strong>
                </div>

                <button className="plan-card__action" type="button" onClick={() => {
                  handleSelectPlan(plan);
                }}>
                  Select plan
                </button>
              </article>
            ))}
          </div>
        )}
      </section>
    </main>
  );
}
