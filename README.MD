# Spring Boot Masterclass: Repositorio de Prácticas

Este repositorio contiene el código fuente, los ejercicios prácticos y los apuntes teóricos desarrollados durante el curso *"Spring Boot 4: Learn Spring 7, Spring Core, Spring REST, Spring Security, JPA, Hibernate, Swagger, Spring MVC, MySQL"* de Chad Darby.

El objetivo de este proyecto es documentar la progresión técnica desde los fundamentos arquitectónicos del framework hasta la construcción de APIs RESTful conectadas a bases de datos relacionales.

## Tecnologías Utilizadas

* **Lenguaje:** Java 17 (LTS)
* **Framework Core:** Spring Boot 3.x / Spring Framework 6.x
* **Gestor de Construcción:** Maven (vía Maven Wrapper local)
* **Entorno de Desarrollo:** Visual Studio Code (VS Code)

## Estructura del Repositorio

El repositorio está organizado en subdirectorios que corresponden a las secciones del curso. Cada carpeta es un proyecto Spring Boot independiente y contiene su propio archivo `README.md` con el resumen teórico detallado de los conceptos aplicados.

* **/01-spring-boot-quickstart:** Fundamentos de inicialización. Demuestra la auto-configuración del framework (`@SpringBootApplication`), la creación de endpoints básicos (`@RestController`, `@GetMapping`) y el manejo de propiedades externas (`application.properties`).
* **/02-spring-core:** Fundamentos arquitectónicos. Implementación de los patrones de Inversión de Control (IoC) e Inyección de Dependencias (DI). Cubre la gestión del contenedor de Spring, la resolución de ambigüedades (`@Primary`, `@Qualifier`), el alcance de los objetos (Singleton vs Prototype) y el ciclo de vida de los Beans (`@PostConstruct`, `@PreDestroy`).
* **/03-hibernate-jpa-crud:** Persistencia de datos. Implementación del patrón de diseño DAO y mapeo objeto-relacional (ORM) utilizando Hibernate y la API de JPA (`@Entity`, `@Id`, `@GeneratedValue`). Gestión del contexto de persistencia mediante `EntityManager`, control automático de transacciones con `@Transactional` y consultas a objetos con JPQL.
* **/04-spring-boot-rest-crud:** Construcción de APIs RESTful corporativas. Arquitectura limpia de tres capas (Controller, Service, Repository), manejo global y estructurado de excepciones (`@ControllerAdvice`, `@ExceptionHandler`) y automatización del acceso a datos mediante Spring Data JPA y Spring Data REST. Incluye también la generación de documentación interactiva con OpenAPI y Swagger UI.
* **/05-spring-boot-rest-security:** Protección y blindaje de APIs REST. Implementación de Spring Security para autenticación y autorización basada en roles (RBAC). Evolución desde configuraciones en memoria (In-Memory) hacia la validación contra bases de datos (JDBC) con contraseñas encriptadas (BCrypt). Incluye la arquitectura avanzada de seguridad integrando entidades JPA/Hibernate personalizadas mediante `UserDetailsService`.
* **/06-spring-boot-spring-mvc:** Desarrollo de aplicaciones web con el patrón Model-View-Controller. Renderización de vistas dinámicas en el servidor utilizando **Thymeleaf** (Data Binding, atributos `th:`). Implementación de validaciones robustas de formularios con **Jakarta Bean Validation** (`@Valid`, `@NotNull`, `@Size`, `@Pattern`). Manejo de errores con `BindingResult`, personalización de mensajes (`messages.properties`), pre-procesamiento de peticiones con `@InitBinder` y creación de anotaciones de validación personalizadas (Custom Constraints).
* **/07-spring-boot-spring-mvc-crud:** Integración Full-Stack. Unión de Spring MVC, Thymeleaf y Spring Data JPA para construir una aplicación web completa con operaciones CRUD (Create, Read, Update, Delete). Implementación de buenas prácticas arquitectónicas como el patrón **Post/Redirect/Get (PRG)** para el manejo seguro de formularios, data binding avanzado para pre-rellenar datos en vistas de actualización, y generación de enlaces dinámicos con parámetros de consulta.

*(Próximamente: Spring Security con Thymeleaf, AOP, etc.)*

## Ejecución Local

Cada sección funciona como una aplicación web autoejecutable gracias al servidor Tomcat embebido. Para ejecutar cualquiera de los proyectos localmente, abre tu terminal, navega a la carpeta deseada y utiliza el Maven Wrapper incluido. No se requiere instalación global de Maven.

```bash
# 1. Navegar al directorio del proyecto específico
cd nombre-de-la-carpeta

# 2. Ejecutar la aplicación
./mvnw spring-boot:run