import axios from 'axios';

export const httpClient = axios.create({
    baseURL: '/api', 
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000, 
});

httpClient.interceptors.response.use(
    (response) => response,
    (error) => {
        console.error('Error en la llamada HTTP:', error.response?.data || error.message);
        return Promise.reject(error);
    }
);