import { useState } from "react";
import {
  checkSubscriptionServiceHealth,
  checkRecurringEngineHealth,
} from "../api/healthApi";

export function DashboardPage() {
  const [healthStatusSubscription, setHealthStatusSubscription] = useState<boolean | null>(null);
    const [healthStatusRecurring, setHealthStatusRecurring] = useState<boolean | null>(null);

  return (
    <div>
      <h1>Dashboard</h1>

      <h1>Health Check - Subscription Service</h1>
      <button
        onClick={async () => {
          try {
            setHealthStatusSubscription(await checkSubscriptionServiceHealth());
          } catch (error) {
            console.error("Error checking subscription service health:", error);
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
            setHealthStatusRecurring(await checkRecurringEngineHealth());
          } catch (error) {
            console.error("Error checking recurring engine health:", error);
          }
        }}
      >
        Health Check
      </button>
      <p>Health Status: {healthStatusRecurring ? 'OK' : 'Not OK'}</p>
    </div>
  );
}
