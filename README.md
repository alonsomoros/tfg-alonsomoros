# TFG - Alonso Moros Villalba
## Diseño e implementación de un sistema de gestión de pagos recurrentes basado en suscripciones digitales 

[![CI Pipeline](https://github.com/alonsomoros/tfg-proyecto/actions/workflows/ci.yml/badge.svg)](https://github.com/alonsomoros/tfg-proyecto/actions)
[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://java.oracle.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

Sistema distribuido para la gestión integral de suscripciones SaaS y cobros recurrentes en segundo plano (*Off-Session*). Desarrollado aplicando **Arquitectura Hexagonal**, **Domain-Driven Design (DDD)** y patrones de microservicios.

## Arquitectura del Sistema

El proyecto se divide en tres componentes principales:
1. **Frontend (React + Vite):** Interfaz de usuario que cumple con la normativa PCI-DSS, tokenizando las tarjetas directamente contra las pasarelas sin que los datos sensibles toquen el backend.
2. **Subscription Service (Puerto 8080):** Microservicio Core. Domina la lógica de negocio, planes, fechas de renovación y gestiona la Máquina de Estados (PENDING, ACTIVE, PAST_DUE). Incluye el motor de CRON Jobs.
3. **Recurring Engine (Puerto 8081):** Microservicio integrador. Utiliza el **Patrón Estrategia (Strategy)** para orquestar la comunicación criptográfica con proveedores externos (Stripe y PayPal) aislando esta complejidad del negocio.

## Stack Tecnológico

* **Backend:** Java 21, Spring Boot, Spring Data JPA, OpenFeign.
* **Frontend:** React, Vite, TypeScript.
* **Bases de Datos:** PostgreSQL (Producción/Docker), H2 (Testing).
* **Integración Continua (CI):** GitHub Actions, Maven Surefire, JUnit 5, Mockito.
* **Infraestructura:** Docker, Docker Compose.
* **Pasarelas de Pago:** Stripe (PaymentIntents), PayPal (Vault API / Mocked Reference Transactions).

---

## Guía de Instalación y Despliegue

Este proyecto está *Containerizado*, lo que significa que puedes desplegar todo el backend y sus bases de datos sin necesidad de instalar Java o PostgreSQL en tu máquina.

### 1. Requisitos Previos
* [Docker](https://www.docker.com/) y Docker Compose instalados.
* [Node.js](https://nodejs.org/) (solo para levantar el Frontend local).
* Git.

### 2. Clonar el repositorio
```bash
git clone https://github.com/alonsomoros/tfg-proyecto.git
cd tfg-proyecto
```

### 3. Configuración de Variables de Entorno (.env)
Por seguridad, las credenciales no están incluidas en el repositorio. Se deberán crear dos archivos como definido en los **.env.example**:

Archivo **.env.subscription**
```.env
APP_PORT=
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_DB=
POSTGRES_PORT=
RECURRING_ENGINE_URL=
```

Archivo **.env.recurring**
```.env
APP_PORT=
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_DB=
POSTGRES_PORT=
STRIPE_API_KEY=
PAYPAL_CLIENT_ID=
PAYPAL_CLIENT_SECRET=
```

Archivo **.env** (En el root)
```.env
SUBSCRIPTION_DB_PORT=5432
RECURRING_DB_PORT=5433
SUBSCRIPTION_APP_PORT=8080
RECURRING_APP_PORT=8081
```

Archivo **.env** (/frontend)
```.env
VITE_STRIPE_PUBLIC_KEY=
VITE_PAYPAL_CLIENT_ID=
VITE_SUBSCRIPTION_SERVICE_URL=
VITE_RECURRING_ENGINE_URL=
```

Puedes usar este comando para copiar los ejemplos:
```bash
cp .env.example .env
cp .env.subscription.example .env.subscription
cp .env.recurring.example .env.recurring
```

Pero deberás rellenar igualmente las variables Stripe/PayPal en .env.recurring

### 4. Despliegue del Ecosistema Backend (Docker)
Levanta todos los microservicios y bases de datos con un solo comando:

```bash
docker-compose up --build -d
```

Comprobación: Puedes ejecutar docker ps para verificar que los 4 contenedores (2 de Postgres y 2 de Spring Boot) están corriendo (Healthy).

### 5. Arranque del Frontend
Abre una nueva terminal, ve a la carpeta de tu frontend y ejecuta:

```bash
cd frontend-app
npm install
npm run dev
```

La aplicación web estará disponible en http://localhost:5173.

---

### Flujo de Demostración (Time-Travel Testing)
Para comprobar el correcto funcionamiento del motor desatendido:  
1. Entra al **Frontend** y realiza una **suscripción** (Se guardará en BBDD como ACTIVE con fecha de próximo cobro en 1 mes/año).  
2. Modifica la fecha de cobro de la subscripción creada de **dos** maneras distintas:  
  2.1. Entra en la **BBDD** y modifica el valor **'next_payment_date'** a la fecha actual.  
  2.2. Ejecuta el Endpoint **/subscriptions/{UUID}** con **Body: {"newPaymentDate": "YYYY-MM-DD"}**.  
3. Dispara el **CRON Job** manualmente usando el endpoint de pruebas oculto:  

```bash
curl -X POST http://localhost:8080/api/v1/jobs/trigger-billing
```
4. Observa los logs del contenedor (docker logs tfg-recurring-app).
Verás cómo el sistema detecta el **cobro pendiente**, selecciona dinámicamente la **pasarela de pago** (Stripe/PayPal), ejecuta un **cobro Off-Session** (MIT) y actualiza automáticamente la **fecha al siguiente ciclo**.

