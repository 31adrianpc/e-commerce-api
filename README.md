
# E-commerce API

API REST para sistema de e-commerce desarrollada con Java 17 y Spring Boot 3.5.4.

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.5.4**
- **MySQL 8.0**
- **Spring Data JPA**
- **Maven**
- **Lombok**

## 🚀 Setup Local

### Prerrequisitos

- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Configuración

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/31adrianpc/e-commerce-api.git
   cd e-commerce-api
   ```

2. **Configurar Base de Datos**
   ```bash
   # Ejecutar el script DDL.sql en MySQL
   mysql -u root -p < DDL.sql
   ```

3. **Configurar aplicación**
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   # Editar application.properties con tus credenciales de BD
   ```

4. **Ejecutar aplicación**
   ```bash
   mvn spring-boot:run
   ```

## 📁 Estado Actual del Proyecto

### ✅ Completado
- ✅ Configuración inicial de Spring Boot
- ✅ Estructura de base de datos (DDL.sql)
- ✅ Entidades con relaciones JPA
- ✅ Configuración de MySQL y JPA

### 🚧 En Desarrollo
- 🚧 Repositories con Spring Data JPA
- 🚧 Services con lógica de negocio
- 🚧 REST Controllers
- 🚧 Seguridad JWT
- 🚧 DTOs y validaciones

## 🗄️ Modelo de Base de Datos

- **Users**: Gestión de usuarios (Customer/Admin)
- **Categories**: Categorías de productos
- **Products**: Catálogo de productos  
- **Carts**: Carritos de compras
- **Orders**: Sistema de órdenes

*Ver DDL.sql para el esquema completo de la base de datos.*

## 🏗️ Arquitectura

Siguiendo el patrón tradicional de capas:
```
Controller → Service → Repository → Entity
```

Con enfoque de interfaces + implementaciones para mejor mantenibilidad.

## 📄 Licencia
Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE para más detalles.

---

*Proyecto en desarrollo activo. README será actualizado conforme avance la implementación.*