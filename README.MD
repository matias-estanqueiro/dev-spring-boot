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
* *(Próximamente: Acceso a datos con Hibernate/JPA, APIs REST avanzadas, Spring Security, etc.)*

## Ejecución Local

Cada sección funciona como una aplicación web autoejecutable gracias al servidor Tomcat embebido. Para ejecutar cualquiera de los proyectos localmente, abre tu terminal, navega a la carpeta deseada y utiliza el Maven Wrapper incluido. No se requiere instalación global de Maven.

```bash
# 1. Navegar al directorio del proyecto específico
cd nombre-de-la-carpeta

# 2. Ejecutar la aplicación
./mvnw spring-boot:run