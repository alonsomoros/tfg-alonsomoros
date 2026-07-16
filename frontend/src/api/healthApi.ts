import { httpClient } from './httpClient'

export const checkSubscriptionServiceHealth = async () => {
    try {
        // GET a http://localhost:8080/subscriptions/health
        const response = await httpClient.get('/subscriptions/health');
        return response.data;
    } catch (error) {
        throw new Error('El backend de Subscription Service está caído');
    }
};

export const checkRecurringEngineHealth = async () => {
    try {
        // GET a http://localhost:8081/recurring-engine/health
        const response = await httpClient.get('/recurring-engine/health');
        return response.data;
    } catch (error) {
        throw new Error('El backend de Recurring Engine está caído');
    }
};