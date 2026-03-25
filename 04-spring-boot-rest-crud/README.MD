# Sección 4: REST APIs, Spring Data JPA y Spring Data REST

Este documento es una referencia detallada y exhaustiva de la Sección 4 del curso. Cubre la creación de APIs RESTful desde cero, el manejo profesional de excepciones y la evolución arquitectónica hacia la automatización extrema con Spring Data.

---

## 1. Fundamentos: REST y JSON

### Arquitectura REST (Representational State Transfer)
REST no es una tecnología ni una librería, es un **estilo de arquitectura** para diseñar aplicaciones en red. 
* **Stateless (Sin estado):** El servidor no guarda información sobre las sesiones del cliente. Cada petición HTTP debe contener toda la información necesaria para ser procesada.
* **Interfaz Uniforme:** Se utilizan los métodos HTTP estándar (`GET`, `POST`, `PUT`, `DELETE`, `PATCH`) sobre URIs lógicas que representan "Recursos" (ej. `/api/employees`).

### JSON (JavaScript Object Notation)
Es el formato estándar de la industria para el intercambio de datos. Es texto plano, ligero y agnóstico del lenguaje (legible tanto por humanos como por máquinas). Los datos se estructuran en pares de `clave: valor`, arreglos `[]` y objetos `{}`.

---

## 2. El Motor de Conversión: POJOs y Jackson

Para que Spring Boot pueda hablar en JSON, utiliza una librería integrada llamada **Jackson** (el proyecto se llama Jackson Data-Binding).

* **Java POJO (Plain Old Java Object):** Es una clase Java simple y pura. No extiende de clases de frameworks pesados. Solo tiene atributos privados, un constructor vacío y métodos Getters/Setters.
* **El Proceso Jackson:**
  * **Serialización (Java a JSON):** Cuando tu Controller retorna un POJO, Jackson llama a los métodos `get` por detrás para leer los valores y construir el JSON de respuesta.
  * **Deserialización (JSON a Java):** Cuando un cliente envía un JSON (ej. en un `POST`), Jackson instancia el POJO (usando el constructor vacío) y llama a los métodos `set` para poblar el objeto con los datos del JSON.

---

## 3. Códigos de Estado HTTP

Un pilar de las buenas prácticas REST es devolver el código HTTP correcto según el resultado de la operación.

| Rango | Categoría | Significado y Ejemplos Comunes |
| :--- | :--- | :--- |
| **100-199** | Informativo | El servidor recibió la petición y continúa procesándola. (Poco usados en CRUD básico). |
| **200-299** | **Éxito** | La operación fue exitosa. <br> `200 OK` (Lectura exitosa), `201 Created` (Recurso creado exitosamente), `204 No Content` (Borrado exitoso). |
| **300-399** | Redirección | El cliente debe tomar una acción adicional para completar la petición (ej. el recurso se movió a otra URL). |
| **400-499** | **Error del Cliente** | El cliente envió algo mal. <br> `400 Bad Request` (JSON inválido), `401 Unauthorized` (Falta login), `403 Forbidden` (Sin permisos), `404 Not Found` (El ID no existe). |
| **500-599** | **Error del Servidor** | El código falló (Excepciones en Java, caída de base de datos). <br> `500 Internal Server Error`. |

---

## 4. Diseño de APIs REST: Best Practices & Bad Practices

Diseñar URLs limpias es vital para que otros desarrolladores puedan consumir tu API intuitivamente.

| Concepto | ❌ Bad Practice (Antipatrón) | ✅ Best Practice (Estándar REST) |
| :--- | :--- | :--- |
| **Nombres vs Verbos** | `/getEmployees`, `/createEmployee` | `/employees` (El verbo lo define el método HTTP: GET o POST) |
| **Singular vs Plural** | `/employee/1` | `/employees/1` (Siempre usar sustantivos en plural) |
| **Jerarquía lógica** | `/employees/1/getManager` | `/employees/1/manager` |
| **Uso de IDs en POST** | Enviar el ID en el JSON para crearlo | Ignorar el ID enviado; la BD debe auto-generarlo. |

---

## 5. El Dilema del `ID`: Tipos Primitivos vs Wrappers

Al definir la Clave Primaria en tu clase `@Entity` (`Employee`), el tipo de dato que elijas cambia drásticamente cómo se comporta Spring Data JPA al guardar:

* **Primitivo (`private long id;`):** Por defecto en Java, un `long` sin inicializar vale `0`. Spring asume que es un registro **NUEVO** si `id == 0`.
* **Wrapper (`private Long id;`):** Por defecto, un objeto `Long` vale `null`. Spring asume que es un registro **NUEVO** si `id == null`.

**La Regla de Oro:** Utiliza siempre Wrappers (`Long` o `Integer`). En tus métodos `POST`, fuerza el ID a `null` (`employee.setId(null);`) para garantizar que JPA ejecute un `INSERT` y evitar el error *Row was updated or deleted by another transaction*.

---

## 6. Actualizaciones Parciales (PATCH) y los RFCs

A diferencia de `PUT` (que sobrescribe el objeto entero), `PATCH` actualiza solo los campos especificados. La industria tiene dos estándares formales para implementar esto:

* **RFC 6902 (JSON Patch):** Es un formato avanzado donde el cliente envía un arreglo de *operaciones* o "instrucciones". Ej: `[{"op": "replace", "path": "/firstName", "value": "Juan"}]`. Es muy poderoso pero complejo de implementar.
* **RFC 7386 (JSON Merge Patch):** Es el enfoque pragmático y más común (el que se simula en el curso con `ObjectMapper`). El cliente simplemente envía un JSON parcial: `{"firstName": "Juan"}`. El servidor fusiona este JSON con el objeto original de la base de datos.

---

## 7. Manejo Global de Excepciones (Exception Handling)

Para evitar devolver las trazas de error crudas de Java al cliente (lo cual es inseguro y poco profesional), centralizamos el manejo de errores.

**Flujo de Implementación de 3 Pasos:**
1. **Crear el POJO de Respuesta (`ErrorResponse`):** Una clase simple con atributos como `status`, `message` y `timeStamp` para estandarizar el JSON de error.
2. **Crear Excepciones Personalizadas:** Clases que heredan de `RuntimeException` (ej. `EmployeeNotFoundException`, `EmployeeBadRequestException`).
3. **Crear el Gestor Global (`@ControllerAdvice`):** Una clase que actúa como un filtro interceptor para toda la aplicación.
   * Contiene métodos anotados con `@ExceptionHandler`.
   * Retorna un `ResponseEntity`, que es una clase especial de Spring que permite envolver el POJO de respuesta (`body`) y forzar el código HTTP exacto (`status`, ej. 404 o 400).

---

## 8. La Capa de Negocio: Flujo de la clase `@Service`

El Controlador nunca debe contener lógica de negocio ni tocar la base de datos. Todo se delega a la capa de Servicio.

1. **La Interfaz (`EmployeeService`):** Define los métodos del negocio.
2. **La Implementación (`EmployeeServiceImpl`):** Se anota con `@Service`.
3. **Inyección de Dependencias:** Se inyecta el DAO/Repository a través del constructor.
4. **Transaccionalidad:** Es aquí, sobre los métodos de la clase `ServiceImpl` (y no en el DAO), donde se debe colocar la anotación `@Transactional` para manejar los *commits* y *rollbacks*.

---

## 9. Análisis Profundo: Spring Data JPA

Spring Data JPA es la revolución que elimina la necesidad de escribir clases de implementación DAO (Data Access Object) repetitivas.

* **¿Cómo funciona?** Simplemente creas una interfaz que herede de `JpaRepository<Entity, ID_Type>`. (Ej: `public interface EmployeeRepository extends JpaRepository<Employee, Long>`).
* **La Magia:** Durante la inicialización de la aplicación, Spring escanea esta interfaz y crea dinámicamente la implementación en memoria. Te regala automáticamente métodos como `findAll()`, `findById()`, `save()` y `deleteById()`. Ya incluyen `@Transactional` por debajo.
* **Query Methods (Métodos de Consulta):** Si necesitas búsquedas personalizadas, solo debes declarar el nombre del método siguiendo un patrón estricto y Spring Data creará la consulta SQL automáticamente. Ej: `List<Employee> findByLastName(String lastName);`.

---

## 10. Automatización Extrema: Spring Data REST

Spring Data REST lleva la magia un paso más allá. Si tu objetivo es exponer operaciones CRUD básicas sobre una base de datos, esta herramienta **elimina por completo la necesidad de programar los `@RestController` y los `@Service`**.

* **¿Cómo funciona?** Al agregar su dependencia en el `pom.xml`, Spring Data REST escanea tus `JpaRepository` y automáticamente genera y expone los endpoints HTTP (GET, POST, PUT, PATCH, DELETE) para esa entidad.
* **Configuración Clave:** Por defecto, expone los recursos en la raíz de la URL y pluraliza la entidad. Se debe configurar en `application.properties`: `spring.data.rest.base-path=/api`.
* **HATEOAS (Hypermedia As The Engine Of Application State):** Las respuestas de Spring Data REST no son simples JSONs. Vienen en formato **HAL (Hypertext Application Language)**. Incluyen un bloque `_links` que proporciona las URLs para acceder a ese recurso específico o a sus recursos relacionados, permitiendo que el cliente "navegue" la API dinámicamente sin hardcodear URLs.

---

## 11. Glosario de Anotaciones (Sección 4)

| Anotación | Nivel | Descripción y Uso |
| :--- | :--- | :--- |
| `@RestController` | Clase | Combina `@Controller` y `@ResponseBody`. Indica que todos los métodos de esta clase devolverán datos (JSON) y no vistas HTML. |
| `@RequestMapping("/ruta")` | Clase / Método | Define la URL base para el controlador o mapea una petición genérica. |
| `@GetMapping`, `@PostMapping` <br> `@PutMapping`, `@DeleteMapping`, `@PatchMapping` | Método | Atajos de `@RequestMapping` específicos para cada verbo HTTP. |
| `@PathVariable` | Parámetro | Extrae un valor directamente de la URI. Ej: `/api/employees/{id}` se captura con `@PathVariable Long id`. |
| `@RequestBody` | Parámetro | Deserializa el JSON enviado en el cuerpo de la petición HTTP y lo convierte en un POJO Java. |
| `@ControllerAdvice` | Clase | Marca una clase como un interceptor global de excepciones que se aplica a todos los `@RestController` del proyecto. |
| `@ExceptionHandler` | Método | (Dentro del ControllerAdvice) Define qué método debe ejecutarse cuando se lanza una excepción específica en el código. |

## 12. Documentación de APIs: OpenAPI y Swagger

En la industria, una API no sirve de nada si los equipos de Frontend o QA no saben cómo consumirla. En lugar de escribir documentos de Word o wikis manualmente, Spring Boot permite generar documentación interactiva y en tiempo real.

* **OpenAPI (Anteriormente Swagger Specification):** Es el estándar de la industria (el formato o contrato subyacente, habitualmente en JSON o YAML) que describe exactamente qué hace tu API, qué endpoints tiene, y qué parámetros o cuerpos (bodies) requiere.
* **Swagger UI:** Es la interfaz gráfica web construida a partir de la especificación OpenAPI. Permite a los desarrolladores explorar la API y ejecutar peticiones directamente desde el navegador (como si fuera un Postman/Thunder Client integrado).

### Implementación en Spring Boot 3
Para proyectos modernos con Spring Boot 3, la librería clásica (`Springfox`) está obsoleta. El estándar actual es **Springdoc OpenAPI**.
Basta con agregar una única dependencia en el `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
</dependency>
```
Al arrancar la aplicación, la magia ocurre automáticamente:
* **JSON crudo de la especificación:** Disponible en `http://localhost:8080/v3/api-docs`
* **Interfaz Gráfica (Swagger UI):** Disponible en `http://localhost:8080/swagger-ui.html`

*(Nota: Springdoc lee automáticamente tus anotaciones `@RestController`, `@GetMapping`, etc., y genera la documentación sin necesidad de escribir código adicional).*

---

## 13. Glosario de `application.properties` (Sección 4)

Durante esta sección, la configuración de la aplicación se vuelve vital para controlar el comportamiento del framework y la base de datos. Aquí están las propiedades clave utilizadas:

### Configuración de la Base de Datos (DataSource)

| Propiedad | Ejemplo de Valor | Descripción |
| :--- | :--- | :--- |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/demo` | La cadena de conexión (Connection String) exacta al motor de base de datos. |
| `spring.datasource.username` | `root` | Usuario con permisos para acceder a la BD. |
| `spring.datasource.password` | `1234` | Contraseña de la BD. |

### Configuración de JPA / Hibernate

| Propiedad | Ejemplo de Valor | Descripción |
| :--- | :--- | :--- |
| `spring.jpa.show-sql` | `true` | Imprime en la consola de Spring Boot todas las sentencias SQL que Hibernate ejecuta por debajo. (Útil para debugging). |
| `spring.jpa.hibernate.ddl-auto` | `update` | Define cómo Hibernate maneja el esquema de la BD. `update` crea o modifica tablas basándose en las clases `@Entity`. (Usar solo en desarrollo; en producción debe ser `none` o `validate`). |

### Configuración de Spring Data REST

| Propiedad | Ejemplo de Valor | Descripción |
| :--- | :--- | :--- |
| `spring.data.rest.base-path` | `/api` | Cambia la ruta raíz desde la cual Spring Data REST expone los endpoints generados mágicamente. (Por defecto es `/`, con esto pasa a ser `/api/entidades`). |
| `spring.data.rest.default-page-size` | `20` | (Opcional) Define cuántos registros por página devolverá Spring Data REST por defecto cuando maneja grandes volúmenes de datos. |

### Configuración de OpenAPI / Swagger (Springdoc)

| Propiedad | Ejemplo de Valor | Descripción |
| :--- | :--- | :--- |
| `springdoc.swagger-ui.path` | `/employee-API` | Personaliza la URL para acceder a la interfaz gráfica interactiva de Swagger UI (reemplaza la ruta `/swagger-ui.html` por defecto). |
| `springdoc.api-docs.path` | `/employee-API-docs` | Personaliza la URL donde se expone el documento JSON crudo de la especificación OpenAPI (reemplaza la ruta `/v3/api-docs` por defecto). |