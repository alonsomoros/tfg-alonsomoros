import { describe, expect, it, vi, beforeEach } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { PlansPage } from '../PlansPage';
import { mockPlan, renderWithRouter } from '../../test/testUtils';

const mockedGetPlans = vi.fn();

vi.mock('../../api/subscriptionServiceApi', () => ({
  getPlans: (...args: unknown[]) => mockedGetPlans(...args),
}));

const navigateMock = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return {
    ...actual,
    useNavigate: () => navigateMock,
  };
});

describe('PlansPage', () => {
  beforeEach(() => {
    navigateMock.mockReset();
    mockedGetPlans.mockReset();
  });

  it('shows loading state while fetching plans', () => {
    mockedGetPlans.mockReturnValue(new Promise(() => undefined));

    renderWithRouter(<PlansPage />);

    expect(screen.getByText('Loading plans...')).toBeInTheDocument();
  });

  it('renders plans returned by the API', async () => {
    mockedGetPlans.mockResolvedValueOnce([mockPlan]);

    renderWithRouter(<PlansPage />);

    expect(await screen.findByText('Basic Plan')).toBeInTheDocument();
    expect(screen.getByText('Entry level subscription')).toBeInTheDocument();
    expect(screen.getByText('BASIC')).toBeInTheDocument();
  });

  it('shows an error message when plan loading fails', async () => {
    mockedGetPlans.mockRejectedValueOnce(new Error('Could not load available plans.'));

    renderWithRouter(<PlansPage />);

    expect(await screen.findByText('Could not load available plans.')).toBeInTheDocument();
  });

  it('stores selected plan and navigates to subscription form', async () => {
    mockedGetPlans.mockResolvedValueOnce([mockPlan]);
    const user = userEvent.setup();

    renderWithRouter(<PlansPage />);

    await user.click(await screen.findByRole('button', { name: 'Select plan' }));

    expect(localStorage.getItem('selectedPlan')).toBe(JSON.stringify(mockPlan));
    expect(navigateMock).toHaveBeenCalledWith('/subscription-form');
  });

  it('sets the document title on mount', async () => {
    mockedGetPlans.mockResolvedValueOnce([]);

    renderWithRouter(<PlansPage />);

    await waitFor(() => {
      expect(document.title).toBe('Subscription Plans');
    });
  });
});
