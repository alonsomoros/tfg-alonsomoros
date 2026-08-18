import { describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { PayPalPaymentForm } from '../PayPalPaymentForm';

vi.mock('@paypal/react-paypal-js', () => ({
  PayPalScriptProvider: ({ children }: { children: React.ReactNode }) => (
    <div data-testid="paypal-provider">{children}</div>
  ),
  PayPalButtons: ({
    onApprove,
    onError,
  }: {
    onApprove?: (data: { orderID?: string }) => void;
    onError?: (error: unknown) => void;
  }) => (
    <div>
      <button type="button" onClick={() => onApprove?.({ orderID: 'paypal_order_123' })}>
        Approve PayPal
      </button>
      <button type="button" onClick={() => onError?.(new Error('PayPal failed'))}>
        Trigger PayPal error
      </button>
    </div>
  ),
}));

describe('PayPalPaymentForm', () => {
  it('renders PayPal provider and buttons', () => {
    render(<PayPalPaymentForm onSuccess={vi.fn()} disabled={false} isSubmitting={false} />);

    expect(screen.getByTestId('paypal-provider')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Approve PayPal' })).toBeInTheDocument();
  });

  it('calls onSuccess with order id after approval', async () => {
    const onSuccess = vi.fn();
    const user = userEvent.setup();

    render(<PayPalPaymentForm onSuccess={onSuccess} disabled={false} isSubmitting={false} />);

    await user.click(screen.getByRole('button', { name: 'Approve PayPal' }));

    expect(onSuccess).toHaveBeenCalledWith('paypal_order_123');
  });

  it('shows error message when PayPal fails', async () => {
    const user = userEvent.setup();

    render(<PayPalPaymentForm onSuccess={vi.fn()} disabled={false} isSubmitting={false} />);

    await user.click(screen.getByRole('button', { name: 'Trigger PayPal error' }));

    expect(screen.getByText('Could not load PayPal.')).toBeInTheDocument();
  });
});
