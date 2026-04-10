🏨 Sistema de Reservas - Backend

API REST desarrollada con **Java 17 y Spring Boot**, que implementa autenticación segura con **JWT**, gestión de usuarios y control de reservas.

---
🚀 Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Lombok
- Postman (testing)

---
🔐 Seguridad

El sistema implementa autenticación y autorización basada en **JWT**:

- Registro de usuarios con contraseña encriptada (BCrypt)
- Login con generación de token JWT
- Protección de endpoints mediante filtros de seguridad
- Uso de `Bearer Token` para acceso a recursos protegidos
- Configuración stateless (sin sesiones)

---
🧱 Arquitectura

El proyecto sigue una arquitectura en capas:

- `controller` → Manejo de endpoints
- `service` → Lógica de negocio
- `repository` → Acceso a datos (JPA)
- `model` → Entidades
- `dto` → Transferencia de datos
- `config` → Seguridad (Spring Security + JWT)
- `security` → Filtro JWT

---
📡 API Endpoints

🔓 Auth

POST /auth/register  
POST /auth/login  

🔐 Reservas

GET /reservas (requires JWT)

---

🧪 Testing with Postman

1. Register user
2. Login to get token
3. Use Bearer Token in headers
4. Access protected endpoints
---

Endpoints principales
🔓 Autenticación
➤ Registro de usuario
