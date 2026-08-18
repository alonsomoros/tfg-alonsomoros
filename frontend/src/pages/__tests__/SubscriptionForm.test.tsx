import { beforeEach, describe, expect, it, vi } from 'vitest';
import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { SubscriptionForm } from '../SubscriptionForm';
import { mockPlan, renderWithRouter } from '../../test/testUtils';

const mockedCreateSubscription = vi.fn();
const navigateMock = vi.fn();

vi.mock('../../api/subscriptionServiceApi', () => ({
  createSubscription: (...args: unknown[]) => mockedCreateSubscription(...args),
}));

vi.mock('../../components/payment/StripePaymentForm', () => ({
  StripePaymentForm: ({
    onSuccess,
    disabled,
  }: {
    onSuccess: (token: string) => void;
    disabled: boolean;
  }) => (
    <button
      type="button"
      disabled={disabled}
      onClick={() => onSuccess('pm_test_123')}
    >
      Mock Stripe submit
    </button>
  ),
}));

vi.mock('../../components/payment/PayPalPaymentForm', () => ({
  PayPalPaymentForm: ({
    onSuccess,
    disabled,
  }: {
    onSuccess: (token: string) => void;
    disabled: boolean;
  }) => (
    <button
      type="button"
      disabled={disabled}
      onClick={() => onSuccess('paypal_order_123')}
    >
      Mock PayPal submit
    </button>
  ),
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return {
    ...actual,
    useNavigate: () => navigateMock,
  };
});

describe('SubscriptionForm', () => {
  beforeEach(() => {
    navigateMock.mockReset();
    mockedCreateSubscription.mockReset();
    localStorage.setItem('selectedPlan', JSON.stringify(mockPlan));
    vi.spyOn(window, 'alert').mockImplementation(() => undefined);
  });

  it('redirects to plans page when no plan is stored', () => {
    localStorage.removeItem('selectedPlan');

    renderWithRouter(<SubscriptionForm />);

    expect(navigateMock).toHaveBeenCalledWith('/');
  });

  it('validates email before moving to payment step', async () => {
    const user = userEvent.setup();

    renderWithRouter(<SubscriptionForm />);

    await user.click(screen.getByRole('button', { name: 'Continuar al pago' }));

    expect(
      screen.getByText('Por favor, introduce un correo electrónico válido.'),
    ).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Tus Datos' })).toBeInTheDocument();
  });

  it('moves to payment step with a valid email', async () => {
    const user = userEvent.setup();

    renderWithRouter(<SubscriptionForm />);

    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'user@example.com');
    await user.click(screen.getByRole('button', { name: 'Continuar al pago' }));

    expect(await screen.findByRole('heading', { name: 'Método de Pago' })).toBeInTheDocument();
    expect(screen.getByText(/Comprando como:/i)).toHaveTextContent('user@example.com');
  });

  it('keeps payment submit disabled until terms are accepted', async () => {
    const user = userEvent.setup();

    renderWithRouter(<SubscriptionForm />);

    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'user@example.com');
    await user.click(screen.getByRole('button', { name: 'Continuar al pago' }));

    expect(await screen.findByRole('button', { name: 'Mock Stripe submit' })).toBeDisabled();
  });

  it('creates subscription after stripe payment success', async () => {
    mockedCreateSubscription.mockResolvedValueOnce({ subscriptionId: 'sub_123' });
    const user = userEvent.setup();

    renderWithRouter(<SubscriptionForm />);

    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'user@example.com');
    await user.type(screen.getByLabelText(/Titular de la Tarjeta/i), 'John Doe');
    await user.click(screen.getByRole('button', { name: 'Continuar al pago' }));
    await user.click(screen.getByRole('checkbox'));
    await user.click(await screen.findByRole('button', { name: 'Mock Stripe submit' }));

    await waitFor(() => {
      expect(mockedCreateSubscription).toHaveBeenCalledWith({
        customerEmail: 'user@example.com',
        planId: 'BASIC',
        paymentInfo: {
          provider: 'STRIPE',
          token: 'pm_test_123',
          cardHolder: 'John Doe',
          expiryMonth: '12',
          expiryYear: '2030',
          last4: '0000',
        },
      });
    });

    expect(window.alert).toHaveBeenCalled();
    expect(navigateMock).toHaveBeenCalledWith('/');
  });

  it('shows subscription errors returned by the API', async () => {
    mockedCreateSubscription.mockRejectedValueOnce(
      new Error('You already have an active subscription for this plan with this email.'),
    );
    const user = userEvent.setup();

    renderWithRouter(<SubscriptionForm />);

    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'user@example.com');
    await user.click(screen.getByRole('button', { name: 'Continuar al pago' }));
    await user.click(screen.getByRole('checkbox'));
    await user.click(await screen.findByRole('button', { name: 'Mock Stripe submit' }));

    expect(
      await screen.findByText(
        'You already have an active subscription for this plan with this email.',
      ),
    ).toBeInTheDocument();
  });

  it('switches to PayPal payment method', async () => {
    const user = userEvent.setup();

    renderWithRouter(<SubscriptionForm />);

    await user.type(screen.getByLabelText(/Correo Electrónico/i), 'user@example.com');
    await user.click(screen.getByRole('button', { name: 'Continuar al pago' }));
    await user.click(screen.getByText('PayPal'));

    expect(await screen.findByRole('button', { name: 'Mock PayPal submit' })).toBeInTheDocument();
  });
});
