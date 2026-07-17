import type { SubscriptionPayload } from '../features/types';
import { httpClient } from './httpClient'

export const createSubscription = async (payload: SubscriptionPayload) => {
    try {
        // POST a http://localhost:8080/subscriptions/subscribe
        const response = await httpClient.post('/subscriptions/subscribe', payload);
        console.log('Subscription Response:', response.data);
        return response.data;
    } catch (error) {
        throw new Error('Error al intentar suscribirse');
    }
};
