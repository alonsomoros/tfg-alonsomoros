import axios from 'axios';

export const httpClient = axios.create({
    baseURL: '/api/v1/', 
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000, 
});

httpClient.interceptors.response.use(
    (response) => response,
    (error) => {
        console.error('HTTP request error:', error.response?.data || error.message);
        return Promise.reject(error);
    }
);