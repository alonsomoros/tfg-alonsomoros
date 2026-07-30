import { httpClient } from './httpClient'

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