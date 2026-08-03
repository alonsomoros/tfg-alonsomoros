import type { SubscriptionPayload } from '../features/types';
import { httpClient } from './httpClient'

export const createSubscription = async (payload: SubscriptionPayload) => {
    try {
        // POST http://localhost:8080/subscriptions/subscribe
        const response = await httpClient.post('/subscriptions/subscribe', payload);
        console.log('Subscription Response:', response.data);
        return response.data;
    } catch (error) {
        throw new Error('Failed to create the subscription');
    }
};

export interface PlanResponse {
  code: string;
  name: string;
  description: string;
  amount: number;
  currency: string;
  billingInterval: string;
}

export const getPlans = async () => {
  const response = await httpClient.get<PlanResponse[]>('/getPlans');
  return response.data;
}
