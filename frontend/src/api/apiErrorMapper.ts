import axios from 'axios';

function getBackendMessage(error: unknown): string {
  if (!axios.isAxiosError(error)) {
    return '';
  }

  const data = error.response?.data;
  if (typeof data === 'string') {
    return data;
  }

  if (data && typeof data === 'object' && 'message' in data) {
    const message = (data as { message?: unknown }).message;
    return typeof message === 'string' ? message : '';
  }

  return '';
}

function mapSubscriptionError(status: number, backendMessage: string): string {
  if (status === 409 && backendMessage.toLowerCase().includes('already ongoing')) {
    return 'You already have an active subscription for this plan with this email.';
  }

  if (status === 400) {
    return backendMessage || 'Invalid subscription request. Please review your data and try again.';
  }

  if (status === 502) {
    return 'The payment provider is temporarily unavailable. Please try again in a few minutes.';
  }

  if (status === 500) {
    return 'The subscription service is currently unavailable. Please try again later.';
  }

  return backendMessage || 'Error while processing the subscription.';
}

export function mapApiError(error: unknown, context: 'subscription' | 'payment' | 'plans' | 'health'): string {
  if (!axios.isAxiosError(error)) {
    return 'Unexpected client error. Please try again.';
  }

  const status = error.response?.status;
  const backendMessage = getBackendMessage(error);

  if (!status) {
    return 'Cannot connect to the server. Please check your connection and try again.';
  }

  if (context === 'subscription') {
    return mapSubscriptionError(status, backendMessage);
  }

  if (context === 'payment') {
    if (status === 400) {
      return backendMessage || 'The payment request is invalid.';
    }

    if (status === 502) {
      return 'Payment gateway is currently unavailable. Please try again later.';
    }

    if (status >= 500) {
      return 'Secure payment service is currently unavailable. Please try again later.';
    }

    return backendMessage || 'Payment service error. Please try again.';
  }

  if (context === 'plans') {
    if (status >= 500) {
      return 'Plans are temporarily unavailable. Please try again later.';
    }
    return backendMessage || 'Could not load available plans.';
  }

  if (context === 'health') {
    return 'Backend service is currently unavailable.';
  }

  return backendMessage || 'Unexpected server error.';
}