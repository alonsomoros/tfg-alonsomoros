import { describe, expect, it } from 'vitest';
import { mapApiError } from '../apiErrorMapper';
import { createAxiosError } from '../../test/testUtils';

describe('mapApiError', () => {
  it('returns generic message for non-axios errors', () => {
    expect(mapApiError(new Error('boom'), 'subscription')).toBe(
      'Unexpected client error. Please try again.',
    );
  });

  it('returns connection message when response status is missing', () => {
    const error = createAxiosError(undefined, undefined, 'Network Error');
    expect(mapApiError(error, 'plans')).toBe(
      'Cannot connect to the server. Please check your connection and try again.',
    );
  });

  describe('subscription context', () => {
    it('maps active subscription conflict', () => {
      const error = createAxiosError(409, { message: 'Subscription already ongoing' });
      expect(mapApiError(error, 'subscription')).toBe(
        'You already have an active subscription for this plan with this email.',
      );
    });

    it('maps validation errors with backend message', () => {
      const error = createAxiosError(400, { message: 'Invalid email format' });
      expect(mapApiError(error, 'subscription')).toBe('Invalid email format');
    });

    it('maps payment provider outage', () => {
      const error = createAxiosError(502, { message: 'Gateway down' });
      expect(mapApiError(error, 'subscription')).toBe(
        'The payment provider is temporarily unavailable. Please try again in a few minutes.',
      );
    });

    it('maps internal server errors', () => {
      const error = createAxiosError(500, { message: 'Internal error' });
      expect(mapApiError(error, 'subscription')).toBe(
        'The subscription service is currently unavailable. Please try again later.',
      );
    });
  });

  describe('payment context', () => {
    it('maps invalid payment requests', () => {
      const error = createAxiosError(400, { message: 'Missing customer email' });
      expect(mapApiError(error, 'payment')).toBe('Missing customer email');
    });

    it('maps gateway unavailability', () => {
      const error = createAxiosError(502, { message: 'Stripe unavailable' });
      expect(mapApiError(error, 'payment')).toBe(
        'Payment gateway is currently unavailable. Please try again later.',
      );
    });

    it('maps secure payment service errors', () => {
      const error = createAxiosError(503, { message: 'Service unavailable' });
      expect(mapApiError(error, 'payment')).toBe(
        'Secure payment service is currently unavailable. Please try again later.',
      );
    });
  });

  describe('plans context', () => {
    it('maps server-side plan loading failures', () => {
      const error = createAxiosError(503, { message: 'DB unavailable' });
      expect(mapApiError(error, 'plans')).toBe(
        'Plans are temporarily unavailable. Please try again later.',
      );
    });

    it('returns backend message for client-side plan errors', () => {
      const error = createAxiosError(404, { message: 'Plans not found' });
      expect(mapApiError(error, 'plans')).toBe('Plans not found');
    });
  });

  describe('health context', () => {
    it('returns health-specific message regardless of status', () => {
      const error = createAxiosError(503, { message: 'Down' });
      expect(mapApiError(error, 'health')).toBe('Backend service is currently unavailable.');
    });
  });

  it('extracts plain string backend messages', () => {
    const error = createAxiosError(418, 'Teapot');
    expect(mapApiError(error, 'subscription')).toBe('Teapot');
  });
});
