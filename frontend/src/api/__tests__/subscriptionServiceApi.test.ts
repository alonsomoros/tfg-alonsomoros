import { beforeEach, describe, expect, it, vi } from 'vitest';
import { httpClient } from '../httpClient';
import { createSubscription, getPlans } from '../subscriptionServiceApi';
import { createAxiosError, mockPlan } from '../../test/testUtils';

vi.mock('../httpClient', () => ({
  httpClient: {
    get: vi.fn(),
    post: vi.fn(),
  },
}));

const mockedGet = vi.mocked(httpClient.get);
const mockedPost = vi.mocked(httpClient.post);

describe('subscriptionServiceApi', () => {
  beforeEach(() => {
    mockedGet.mockReset();
    mockedPost.mockReset();
  });

  describe('getPlans', () => {
    it('returns plans from backend', async () => {
      mockedGet.mockResolvedValueOnce({ data: [mockPlan] });

      await expect(getPlans()).resolves.toEqual([mockPlan]);
      expect(mockedGet).toHaveBeenCalledWith('plans/getPlans');
    });

    it('maps backend errors to user-friendly messages', async () => {
      mockedGet.mockRejectedValueOnce(createAxiosError(503, { message: 'DB down' }));

      await expect(getPlans()).rejects.toThrow(
        'Plans are temporarily unavailable. Please try again later.',
      );
    });
  });

  describe('createSubscription', () => {
    const payload = {
      customerEmail: 'user@example.com',
      planId: 'BASIC',
      paymentInfo: {
        provider: 'STRIPE',
        token: 'pm_123',
        cardHolder: 'John Doe',
        expiryMonth: '12',
        expiryYear: '2030',
        last4: '4242',
      },
    };

    it('posts subscription payload and returns response', async () => {
      const response = { subscriptionId: 'sub_123' };
      mockedPost.mockResolvedValueOnce({ data: response });

      await expect(createSubscription(payload)).resolves.toEqual(response);
      expect(mockedPost).toHaveBeenCalledWith('subscriptions/subscribe', payload);
    });

    it('maps subscription conflict errors', async () => {
      mockedPost.mockRejectedValueOnce(
        createAxiosError(409, { message: 'Subscription already ongoing for user' }),
      );

      await expect(createSubscription(payload)).rejects.toThrow(
        'You already have an active subscription for this plan with this email.',
      );
    });
  });
});
