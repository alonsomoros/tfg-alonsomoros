import { httpClient } from './httpClient'
import { mapApiError } from './apiErrorMapper';

export const checkSubscriptionServiceHealth = async () => {
    try {
        // GET http://localhost:8080/api/v1/subscriptions/health
        const response = await httpClient.get('subscriptions/health');
        console.log('Subscription Service Health Check Response:', response.data);
        return response.data;
    } catch (error) {
        throw new Error(mapApiError(error, 'health'));
    }
};

export const checkRecurringEngineHealth = async () => {
    try {
        // GET http://localhost:8081/api/v1/recurring-engine/health
        const response = await httpClient.get('recurring-engine/health');
        console.log('Recurring Engine Health Check Response:', response.data);
        return response.data;
    } catch (error) {
        throw new Error(mapApiError(error, 'health'));
    }
};