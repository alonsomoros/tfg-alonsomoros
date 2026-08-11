import { useState } from "react";
import {
  checkSubscriptionServiceHealth,
  checkRecurringEngineHealth,
} from "../api/healthApi";

export function DashboardPage() {
  const [healthStatusSubscription, setHealthStatusSubscription] = useState<boolean | null>(null);
  const [healthStatusRecurring, setHealthStatusRecurring] = useState<boolean | null>(null);
  const [healthError, setHealthError] = useState('');

  const getErrorMessage = (error: unknown, fallbackMessage: string): string => {
    if (error instanceof Error && error.message) {
      return error.message;
    }
    return fallbackMessage;
  };

  return (
    <div>
      <h1>Dashboard</h1>

      <h1>Health Check - Subscription Service</h1>
      <button
        onClick={async () => {
          try {
            setHealthError('');
            setHealthStatusSubscription(await checkSubscriptionServiceHealth());
          } catch (error) {
            console.error("Error checking subscription service health:", error);
            setHealthError(getErrorMessage(error, 'Subscription service is unavailable.'));
            setHealthStatusSubscription(false);
          }
        }}
      >
        Health Check
      </button>
      <p>Health Status: {healthStatusSubscription ? 'OK' : 'Not OK'}</p>

      <h1>Health Check - Recurring Engine</h1>
      <button
        onClick={async () => {
          try {
            setHealthError('');
            setHealthStatusRecurring(await checkRecurringEngineHealth());
          } catch (error) {
            console.error("Error checking recurring engine health:", error);
            setHealthError(getErrorMessage(error, 'Recurring engine service is unavailable.'));
            setHealthStatusRecurring(false);
          }
        }}
      >
        Health Check
      </button>
      <p>Health Status: {healthStatusRecurring ? 'OK' : 'Not OK'}</p>
      {healthError && <p style={{ color: 'red' }}>{healthError}</p>}
    </div>
  );
}
