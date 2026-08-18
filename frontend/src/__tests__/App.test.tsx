import { describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import App from '../App';
import { mockPlan } from '../test/testUtils';

const mockedGetPlans = vi.fn();

vi.mock('../api/subscriptionServiceApi', () => ({
  getPlans: (...args: unknown[]) => mockedGetPlans(...args),
  createSubscription: vi.fn(),
}));

describe('App routing', () => {
  it('renders plans page on root route', async () => {
    mockedGetPlans.mockResolvedValueOnce([mockPlan]);
    window.history.pushState({}, 'Plans', '/');

    render(<App />);

    expect(await screen.findByText('Choose the plan that fits your business model')).toBeInTheDocument();
  });

  it('renders subscription form route', () => {
    localStorage.setItem('selectedPlan', JSON.stringify(mockPlan));
    window.history.pushState({}, 'Subscription', '/subscription-form');

    render(<App />);

    expect(screen.getByRole('heading', { name: 'Tus Datos' })).toBeInTheDocument();
  });
});
