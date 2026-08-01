import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css'
import { PlansPage } from './pages/PlansPage'
import { SubscriptionForm } from './pages/SubscriptionForm'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<PlansPage />} />
        <Route path="/subscription-form" element={<SubscriptionForm />} />
      </Routes>
    </Router>
  )
}

export default App
