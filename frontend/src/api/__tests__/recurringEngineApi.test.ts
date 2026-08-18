import { beforeEach, describe, expect, it, vi } from 'vitest';
import { httpClient } from '../httpClient';
import { createSetupIntent } from '../recurringEngineApi';
import { createAxiosError } from '../../test/testUtils';

vi.mock('../httpClient', () => ({
  httpClient: {
    get: vi.fn(),
    post: vi.fn(),
  },
}));

const mockedPost = vi.mocked(httpClient.post);

describe('recurringEngineApi', () => {
  beforeEach(() => {
    mockedPost.mockReset();
  });

  it('createSetupIntent posts customer email and returns client secret', async () => {
    mockedPost.mockResolvedValueOnce({ data: { clientSecret: 'seti_secret_123' } });

    await expect(createSetupIntent('user@example.com')).resolves.toEqual({
      clientSecret: 'seti_secret_123',
    });
    expect(mockedPost).toHaveBeenCalledWith('payment-sessions', {
      customerEmail: 'user@example.com',
    });
  });

  it('maps payment gateway errors', async () => {
    mockedPost.mockRejectedValueOnce(createAxiosError(502, { message: 'Stripe down' }));

    await expect(createSetupIntent('user@example.com')).rejects.toThrow(
      'Payment gateway is currently unavailable. Please try again later.',
    );
  });
});
