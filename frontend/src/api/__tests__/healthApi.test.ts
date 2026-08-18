import { beforeEach, describe, expect, it, vi } from 'vitest';
import { httpClient } from '../httpClient';
import {
  checkRecurringEngineHealth,
  checkSubscriptionServiceHealth,
} from '../healthApi';
import { createAxiosError } from '../../test/testUtils';

vi.mock('../httpClient', () => ({
  httpClient: {
    get: vi.fn(),
    post: vi.fn(),
  },
}));

const mockedGet = vi.mocked(httpClient.get);

describe('healthApi', () => {
  beforeEach(() => {
    mockedGet.mockReset();
  });

  it('checkSubscriptionServiceHealth returns backend payload', async () => {
    mockedGet.mockResolvedValueOnce({ data: true });

    await expect(checkSubscriptionServiceHealth()).resolves.toBe(true);
    expect(mockedGet).toHaveBeenCalledWith('subscriptions/health');
  });

  it('checkSubscriptionServiceHealth maps axios errors', async () => {
    mockedGet.mockRejectedValueOnce(createAxiosError(503, { message: 'Down' }));

    await expect(checkSubscriptionServiceHealth()).rejects.toThrow(
      'Backend service is currently unavailable.',
    );
  });

  it('checkRecurringEngineHealth returns backend payload', async () => {
    mockedGet.mockResolvedValueOnce({ data: true });

    await expect(checkRecurringEngineHealth()).resolves.toBe(true);
    expect(mockedGet).toHaveBeenCalledWith('recurring-engine/health');
  });

  it('checkRecurringEngineHealth maps axios errors', async () => {
    mockedGet.mockRejectedValueOnce(createAxiosError(500, { message: 'Error' }));

    await expect(checkRecurringEngineHealth()).rejects.toThrow(
      'Backend service is currently unavailable.',
    );
  });
});
