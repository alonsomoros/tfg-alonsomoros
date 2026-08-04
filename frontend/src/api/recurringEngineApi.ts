import { httpClient } from './httpClient'

export const createSetupIntent = async (): Promise<{ clientSecret: string }> => {
    try {
        const response = await httpClient.post('payment-sessions');
        return response.data; 
    } catch (error) {
        console.error('Error connecting to the Recurring Engine:', error);
        throw new Error('The payment service is not available at this time.');
    }
};