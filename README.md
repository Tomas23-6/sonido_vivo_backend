# Sonido Vivo — Backend API REST (Spring Boot + JWT + MySQL/H2)

Backend desarrollado para la **Evaluación Parcial N° 3 (DSY1104 - Desarrollo Fullstack II)**, caso **Tienda Sonido Vivo (Forma B)**.

## Tecnologías
- **Java 17** + **Spring Boot 3.3.5**
- **Spring Data JPA** (H2 en memoria por defecto / MySQL 8 en perfil `mysql`)
- **Spring Security + JJWT (JWT Access & Refresh Tokens)** con contraseñas encriptadas en BCrypt
- **Springdoc OpenAPI / Swagger UI**
- **Docker & Docker Compose** (listo para despliegue en AWS EC2 / ECS)

## Ejecución Rápida (Modo Desarrollo con H2)
```bash
mvn spring-boot:run
```
- **API Base:** `http://localhost:8080/api/v1`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Consola H2:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:sonidovivodb`, user: `sa`, password vacío)

## Ejecución con MySQL y Docker
```bash
docker-compose up --build
```

## Credenciales Precargadas
- **Administrador:** `admin@sonidovivo.cl` / `Admin1234`
- **Vendedor:** `vendedor@sonidovivo.cl` / `Vendedor1234`
- **Cliente:** `cliente@sonidovivo.cl` / `Cliente1234`
# sonido_vivo_backend
# sonido_vivo_backend
