import axios, { type AxiosError, type InternalAxiosRequestConfig } from 'axios';
import type { ReactElement } from 'react';
import { render, type RenderOptions } from '@testing-library/react';
import { MemoryRouter, type MemoryRouterProps } from 'react-router-dom';

interface RenderWithRouterOptions extends Omit<RenderOptions, 'wrapper'> {
  route?: string;
  routerProps?: MemoryRouterProps;
}

export function renderWithRouter(
  ui: ReactElement,
  { route = '/', routerProps, ...renderOptions }: RenderWithRouterOptions = {},
) {
  return render(
    <MemoryRouter initialEntries={[route]} {...routerProps}>
      {ui}
    </MemoryRouter>,
    renderOptions,
  );
}

export function createAxiosError(
  status?: number,
  data?: unknown,
  message = 'Request failed',
): AxiosError {
  const error = new axios.AxiosError(message);
  error.response = status
    ? {
        status,
        data,
        statusText: 'Error',
        headers: {},
        config: { headers: {} } as InternalAxiosRequestConfig,
      }
    : undefined;
  return error;
}

export const mockPlan = {
  code: 'BASIC',
  name: 'Basic Plan',
  description: 'Entry level subscription',
  amount: 9.99,
  currency: 'EUR',
  billingInterval: 'MONTHLY',
};
