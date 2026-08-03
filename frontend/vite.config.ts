import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/v1/subscriptions': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/api/v1/plans/getPlans': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/api/v1/payment-sessions': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/api/v1/payment-methods': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      }
    }
  }
})