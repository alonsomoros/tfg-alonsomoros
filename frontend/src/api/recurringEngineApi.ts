import { httpClient } from './httpClient'
import { mapApiError } from './apiErrorMapper';

export const createSetupIntent = async (customerEmail: string): Promise<{ clientSecret: string }> => {
    try {
        const response = await httpClient.post('payment-sessions', { customerEmail });
        return response.data; 
    } catch (error) {
        console.error('Error connecting to the Recurring Engine:', error);
        throw new Error(mapApiError(error, 'payment'));
    }
};