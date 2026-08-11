import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [react()],
    server: {
      proxy: {
        '/api/v1/subscriptions': {
          target: env.VITE_SUBSCRIPTION_SERVICE_URL || 'http://localhost:8080',
          changeOrigin: true,
        },
        '/api/v1/plans/getPlans': {
          target: env.VITE_SUBSCRIPTION_SERVICE_URL || 'http://localhost:8080',
          changeOrigin: true,
        },
        '/api/v1/payment-sessions': {
          target: env.VITE_RECURRING_ENGINE_URL || 'http://localhost:8081',
          changeOrigin: true,
        },
        '/api/v1/payment-methods': {
          target: env.VITE_RECURRING_ENGINE_URL || 'http://localhost:8081',
          changeOrigin: true,
        }
      }
    }
  }
})