# 🧩 User-Api - BCI

Api con la cual podemos realizar las acciones CRUD a usuarios, siempre y cuando nos loguiemos con algun usuaurio precargado en Base de datos.


## ✅ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.4**
- **Gradle**
- **Spring Security con JWT**
- **H2 Database**
- **Spring Data JPA**
- **OpenAPI 3 / Swagger**
- **JUnit 5 + Mockito**
- **Notebook Linux (Ubuntu)**

---



## 🚀 Características Destacadas

- Arquitectura hexagonal con enfoque vertical slicing
- API RESTful para gestión de usuarios
- Validaciones con Bean Validation
- Seguridad JWT (HS256)
- Base de datos en memoria (HSQLDB)
- Documentación Swagger/OpenAPI
- Pruebas unitarias con JUnit 5 y Mockito
- Logging por consola y archivo (`logs/api-user.log`)

---

## ⚙️ Requisitos del sistema

- Java 21
- Gradle

## 🚀 Cómo ejecutar la aplicación

1. Clonar y compilar el repositorio:  
   `git clone https://github.com/alvarovergaracortes/api-user.git`  
   `cd api-user`  
   `./gradlew clean build`

2. Ejecutar la aplicación:
   `./gradlew bootRun`

3. Acceder a la documentación Swagger:
   `http://localhost:8082/swagger-ui/index.html`

4. Acceder a la consola H2:  

```plaintext
    URL     : http://localhost:8082/h2-console  
    JDBC URL: jdbc:h2:mem:testdb  
    User    : sa  
    Password: (vacío)  
```

---

## 🧪 Usuarios Pre-cargados
El archivo data.sql, permite cargar datos a la tabla users

```plaintext
| Usuario  | Contraseña | Rol    |  
|----------|------------|--------|  
| `admin`  | `Admin123` | `ADMIN`|  
| `user`   | `User123`  | `USER` |  
```

> *Contraseñas encriptadas con BCrypt (ver clase: cl.bci.common.helper.CreateEncryptedPassword)*

---

## 🔐 Autenticación

1. Inicia sesión con credenciales válidas(email y password) y retorna un token JWT.  

Request Body:  

```
{
    "email": "admin@example.com",
    "password": "Admin123"
}
```
Response (200):

```
{
    "email": "admin@example.com",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1......"
}
```


2. Luego de obtener el Token en el login, podemos realizar las accines CRUD `Authorization`:

```
Authorization
Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1......
```

---
##🛡️ Seguridad
* Se utiliza JWT en el encabezado Authorization: Bearer <token>.  
* Los endpoints de CRUD del usuario requieren estar autenticados.  
* Solo los usuarios con rol ADMIN pueden crear, leer, actualizar y eliminar.  
* Los usuarios con rol USER solo pueden leer ó ver.

---

## 📌 Endpoints principales

### Login
```plaintext

| Método | Endpoint                          | Descripción                    | Acceso  |
|--------|-----------------------------------|--------------------------------|---------|
| POST   | http://localhost:8082/auth/login  | Login y obtención de token     | publico |
```

### CRUD
```plaintext
| Método | Endpoint                          | Descripción                    |
|--------|-----------------------------------|--------------------------------|
| POST   | http://localhost:8082/users       | Crea un nuevo usuario          |
| GET    | http://localhost:8082/users       | Obtiene todos los usuarios     |
| GET    | http://localhost:8082/users/{id}  | Obtiene un usuario por ID      |
| PUT    | http://localhost:8082/users/{id}  | Actualiza un usuario existente |
| DELETE | http://localhost:8082/users/{id}  | Elimina un usuario por ID      |
```

---

## 📄 Validaciones

- `nombre`   : no debe estar vacío
- `email`    : no debe estar vacío, debe cumplir patron especifico.
- `password` : no debe estar vacío, debe cumplir patron especifico.
- `phones`   : debe incluir al menos un fono.
- Solo `ADMIN` puede realizar tareas CRUD.
- Token JWT obligatorio en endpoints protegidos.(acciones Crud)

---

## 🧪 Pruebas Automatizadas

- Test para todos para todos los endpoints
- Casos cubiertos:
  - Éxito y error en login
  - CRUD completo de tareas
  - Validaciones y errores de seguridad

---
## 📂 Estructura del Proyecto

```
src
├── main
│   ├── java/cl/bci
│   │   ├── common/        → Seguridad y manejo global
│   │   └──user/            → Login y crud usuario
│   │       ├── application/
│   │       ├── domain/
│   │       └── infrastructure/
│   │
│   └── resources/
│       ├── data.sql               → Datos precargados
│       ├── application.properties → Configuración general
│       └── schema.sql             → Script de creación de tablas (DDL)
├── test/java/  → Pruebas unitarias
```

## 📁 Documentacion de apoyo

- 📂 `docs/` → Diagramas, instrucciones y ejemplos
- 📄 `desafio-spring-boot.postman_collection.json` → Ejemplos de consumo

---

## 🙋 Autor

**Álvaro Vergara Cortés**  
Correo: alvaro.vergara.cl@gmail.com 
