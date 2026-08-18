import { beforeEach, describe, expect, it, vi } from 'vitest';
import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { DashboardPage } from '../DashboardPage';
import { renderWithRouter } from '../../test/testUtils';

const mockedCheckSubscriptionHealth = vi.fn();
const mockedCheckRecurringHealth = vi.fn();

vi.mock('../../api/healthApi', () => ({
  checkSubscriptionServiceHealth: (...args: unknown[]) =>
    mockedCheckSubscriptionHealth(...args),
  checkRecurringEngineHealth: (...args: unknown[]) => mockedCheckRecurringHealth(...args),
}));

describe('DashboardPage', () => {
  beforeEach(() => {
    mockedCheckSubscriptionHealth.mockReset();
    mockedCheckRecurringHealth.mockReset();
  });

  it('renders health check controls', () => {
    renderWithRouter(<DashboardPage />);

    expect(screen.getByRole('heading', { name: 'Dashboard' })).toBeInTheDocument();
    expect(screen.getAllByRole('button', { name: 'Health Check' })).toHaveLength(2);
  });

  it('shows OK status when subscription service is healthy', async () => {
    mockedCheckSubscriptionHealth.mockResolvedValueOnce(true);
    const user = userEvent.setup();

    renderWithRouter(<DashboardPage />);

    await user.click(screen.getAllByRole('button', { name: 'Health Check' })[0]);

    expect(await screen.findByText('Health Status: OK')).toBeInTheDocument();
  });

  it('shows error when recurring engine health check fails', async () => {
    mockedCheckRecurringHealth.mockRejectedValueOnce(
      new Error('Backend service is currently unavailable.'),
    );
    const user = userEvent.setup();

    renderWithRouter(<DashboardPage />);

    await user.click(screen.getAllByRole('button', { name: 'Health Check' })[1]);

    expect(
      await screen.findByText('Backend service is currently unavailable.'),
    ).toBeInTheDocument();
    expect(screen.getAllByText('Health Status: Not OK')).toHaveLength(2);
  });
});
