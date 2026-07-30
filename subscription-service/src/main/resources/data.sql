INSERT INTO plans (id, code, name, description, amount, currency, billing_interval, is_active, created_at, updated_at) 
VALUES 
    ('d1a3c748-0b54-4a25-83e9-7988350567c9', 'BASIC_MONTHLY', 'Plan Básico', 'Acceso a todas las funciones esenciales', 9.99, 'EUR', 'MONTHLY', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('f7b9273c-628b-4a55-8db5-5626245eb6a0', 'PRO_MONTHLY', 'Plan Profesional', 'Soporte prioritario y sin límites', 19.99, 'EUR', 'MONTHLY', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('2c5d4b53-43ef-4813-8120-e55d5b7a14e9', 'PRO_YEARLY', 'Plan Profesional Anual', 'Ahorro de 2 meses en el plan Pro', 199.90, 'EUR', 'YEARLY', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);