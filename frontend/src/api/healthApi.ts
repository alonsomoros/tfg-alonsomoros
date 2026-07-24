import { httpClient } from './httpClient'

export const checkSubscriptionServiceHealth = async () => {
    try {
        // GET http://localhost:8080/subscriptions/health
        const response = await httpClient.get('/subscriptions/health');
        console.log('Subscription Service Health Check Response:', response.data);
        return response.data;
    } catch (error) {
        throw new Error('Subscription Service backend is unavailable');
    }
};

export const checkRecurringEngineHealth = async () => {
    try {
        // GET http://localhost:8081/recurring-engine/health
        const response = await httpClient.get('/recurring-engine/health');
        console.log('Recurring Engine Health Check Response:', response.data);
        return response.data;
    } catch (error) {
        throw new Error('Recurring Engine backend is unavailable');
    }
};