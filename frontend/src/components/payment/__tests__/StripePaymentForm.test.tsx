import { beforeEach, describe, expect, it, vi } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { StripePaymentForm } from '../StripePaymentForm';

const mockedCreateSetupIntent = vi.fn();
const confirmSetupMock = vi.fn();

vi.mock('../../../api/recurringEngineApi', () => ({
  createSetupIntent: (...args: unknown[]) => mockedCreateSetupIntent(...args),
}));

vi.mock('@stripe/stripe-js', () => ({
  loadStripe: vi.fn(() => Promise.resolve({})),
}));

vi.mock('@stripe/react-stripe-js', () => ({
  Elements: ({ children }: { children: React.ReactNode }) => (
    <div data-testid="stripe-elements">{children}</div>
  ),
  PaymentElement: () => <div data-testid="payment-element" />,
  useStripe: () => ({
    confirmSetup: confirmSetupMock,
  }),
  useElements: () => ({}),
}));

describe('StripePaymentForm', () => {
  beforeEach(() => {
    mockedCreateSetupIntent.mockReset();
    confirmSetupMock.mockReset();
  });

  it('shows loading state while fetching setup intent', () => {
    mockedCreateSetupIntent.mockReturnValue(new Promise(() => undefined));

    render(
      <StripePaymentForm
        customerEmail="user@example.com"
        onSuccess={vi.fn()}
        disabled={false}
        isSubmitting={false}
      />,
    );

    expect(screen.getByText('Connecting to the secure payment gateway...')).toBeInTheDocument();
  });

  it('shows connection error when setup intent fails', async () => {
    mockedCreateSetupIntent.mockRejectedValueOnce(
      new Error('Payment gateway is currently unavailable. Please try again later.'),
    );

    render(
      <StripePaymentForm
        customerEmail="user@example.com"
        onSuccess={vi.fn()}
        disabled={false}
        isSubmitting={false}
      />,
    );

    expect(
      await screen.findByText(
        'Payment gateway is currently unavailable. Please try again later.',
      ),
    ).toBeInTheDocument();
  });

  it('submits stripe setup and calls onSuccess with payment method id', async () => {
    const onSuccess = vi.fn();
    mockedCreateSetupIntent.mockResolvedValueOnce({ clientSecret: 'seti_secret' });
    confirmSetupMock.mockResolvedValueOnce({
      setupIntent: { status: 'succeeded', payment_method: 'pm_123' },
    });
    const user = userEvent.setup();

    render(
      <StripePaymentForm
        customerEmail="user@example.com"
        onSuccess={onSuccess}
        disabled={false}
        isSubmitting={false}
      />,
    );

    await user.click(await screen.findByRole('button', { name: 'Confirmar suscripción' }));

    await waitFor(() => {
      expect(onSuccess).toHaveBeenCalledWith('pm_123');
    });
  });

  it('shows stripe validation errors', async () => {
    mockedCreateSetupIntent.mockResolvedValueOnce({ clientSecret: 'seti_secret' });
    confirmSetupMock.mockResolvedValueOnce({
      error: { message: 'Your card was declined.' },
    });
    const user = userEvent.setup();

    render(
      <StripePaymentForm
        customerEmail="user@example.com"
        onSuccess={vi.fn()}
        disabled={false}
        isSubmitting={false}
      />,
    );

    await user.click(await screen.findByRole('button', { name: 'Confirmar suscripción' }));

    expect(await screen.findByText('Your card was declined.')).toBeInTheDocument();
  });
});
