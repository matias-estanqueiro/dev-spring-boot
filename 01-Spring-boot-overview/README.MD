# Spring Boot QuickStart: Resumen de la Sección 1

Este documento compila los conceptos fundamentales y las herramientas operativas para la inicialización de un proyecto en Spring Boot, basados en la sección 1 del curso.

## 1. Configuración Inicial y Construcción

* **Spring Initializr:** Herramienta oficial para generar la estructura base del proyecto y declarar las dependencias (Starters).
* **Maven (`pom.xml`):** Archivo de configuración que gestiona las dependencias. Spring Boot utiliza *Starters* (ej. `spring-boot-starter-web`) para agrupar librerías compatibles (como Tomcat embebido y Spring MVC) sin necesidad de configurar versiones individuales.
* **Maven Wrapper (`mvnw`):** Script que encapsula la descarga y ejecución de una versión específica de Maven. Garantiza que la construcción del proyecto sea idéntica en cualquier entorno, independientemente de la configuración local del sistema operativo.

## 2. Anotaciones Estructurales

* **`@SpringBootApplication`:** Anotación raíz ubicada en la clase principal (la que contiene el método `main`). Agrupa tres funciones críticas:
    1.  `@EnableAutoConfiguration`: Instruye a Spring a configurar automáticamente *beans* basados en las dependencias detectadas en el classpath (ej. inicializar un servidor web si detecta `spring-web`).
    2.  `@ComponentScan`: Ordena a Spring buscar anotaciones estereotipo (como `@RestController` o `@Component`) dentro del paquete actual y sus subpaquetes para registrarlas en el contexto de la aplicación.
    3.  `@Configuration`: Permite registrar beans adicionales o importar otras clases de configuración.

## 3. Endpoints y Exposición de Datos (REST)

* **`@RestController`:** Marca una clase como un controlador especializado en APIs REST. Garantiza que el retorno de los métodos se serialice automáticamente al cuerpo de la respuesta HTTP (generalmente en formato JSON), en lugar de intentar renderizar una plantilla de vista (como HTML).
* **`@GetMapping("/ruta")`:** Mapea solicitudes HTTP con el verbo GET hacia un método específico en Java. 

## 4. Configuración Externa

* **`application.properties`:** Archivo properties utilizado para sobrescribir la configuración predeterminada del framework (ej. `server.port=8080`) o para definir propiedades de negocio personalizadas.
* **`@Value("${propiedad}")`:** Anotación utilizada para leer e inyectar los valores definidos en `application.properties` directamente en las variables de instancia de una clase Java, eliminando la mala práctica de incrustar valores en el código fuente (hardcoding).

## 5. Herramientas de Operación

* **Spring Boot DevTools:** Dependencia enfocada en el desarrollo local. Su función principal es el reinicio rápido (Fast Restart): recarga automáticamente el contexto de Spring cuando detecta cambios en las clases compiladas, ahorrando el tiempo de un reinicio manual en frío.
* **Spring Boot Actuator:** Módulo enfocado en producción. Expone endpoints HTTP preconfigurados (como `/actuator/health` o `/actuator/info`) que permiten a sistemas externos e infraestructura en la nube monitorear la salud, métricas y el estado del tráfico de la aplicación.

---

## ⚠️ Análisis Arquitectónico Crítico

Los ejercicios iniciales de esta sección implementan la lógica de negocio y la creación de datos directamente dentro del controlador (`@RestController`). **Esto viola el Principio de Responsabilidad Única (SRP)**.

En una arquitectura de software estándar, el controlador solo debe actuar como un enrutador: recibir la petición HTTP, delegar el procesamiento a una capa de servicios (Service Layer) y formatear la respuesta. El controlador no debe utilizar la palabra reservada `new
