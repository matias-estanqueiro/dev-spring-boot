# Curso Spring Boot - Sección 6: Spring MVC, Thymeleaf y Validaciones

Este documento resume los conceptos clave para la creación de aplicaciones web (Frontend y Backend) utilizando el patrón arquitectónico Model-View-Controller (MVC) nativo de Spring, la renderización de vistas con Thymeleaf y la validación robusta de datos de formularios.

---

## 1. El Motor de Plantillas: Thymeleaf

### ¿Qué es?
Thymeleaf es un motor de plantillas del lado del servidor para Java. Su función principal es tomar datos dinámicos generados por nuestro Controlador (la "cápsula" o `Model`) y fusionarlos con archivos HTML estándar para renderizar la vista final que verá el usuario en su navegador. Su gran ventaja es que los archivos `.html` de Thymeleaf pueden abrirse y visualizarse en cualquier navegador sin necesidad de correr el servidor.

### Configuración del Proyecto
Para utilizarlo, basta con:
1. Agregar la dependencia `spring-boot-starter-thymeleaf` en el `pom.xml`.
2. Colocar los archivos `.html` dentro de la carpeta obligatoria `src/main/resources/templates`. Spring Boot auto-configurará todo para buscar las vistas en este directorio.

---

## 2. Glosario de Atributos Thymeleaf (`th:`)

Thymeleaf utiliza atributos personalizados que se agregan directamente a las etiquetas HTML estándar.

| Atributo | Propósito / Explicación |
| :--- | :--- |
| `th:action` | Define la URL a la que se enviarán los datos del formulario (reemplaza al `action` nativo de HTML). Ej: `th:action="@{/processForm}"` |
| `th:object` | Vincula el formulario completo a un objeto Java específico (un POJO) que fue enviado desde el Controlador a través del `Model`. |
| `th:field` | Vincula un input (`<input>`, `<select>`) a una propiedad específica del objeto declarado en `th:object`. Usa la sintaxis de asterisco: `th:field="*{firstName}"`. Se encarga automáticamente de generar los atributos `id`, `name` y `value` de HTML. |
| `th:value` | Establece el valor de un elemento. Muy usado en las etiquetas `<option>` de un menú desplegable. |
| `th:text` | Reemplaza el texto interno de una etiqueta HTML con el valor evaluado. Ej: `<span th:text="${estudiante.nombre}"></span>`. |
| `th:if` | Renderización condicional. Si la condición es `true`, la etiqueta HTML y su contenido se muestran en pantalla; de lo contrario, se eliminan del DOM. |
| `th:errors` | Muestra los mensajes de error de validación asociados a un campo específico del formulario. |
| `th:each` | Bucle for-each. Itera sobre una lista o colección. Ej: `th:each="tempStudent : ${students}"`. |

---

## 3. Validaciones de Datos (Jakarta Bean Validation)

### ¿Qué son y cómo se utilizan?
Las validaciones son reglas estrictas que aplicamos a los atributos de nuestras clases modelo (POJOs) para asegurar que los datos enviados por el usuario sean correctos antes de procesarlos. Se implementan agregando la dependencia `spring-boot-starter-validation` y utilizando anotaciones sobre las variables de la clase. En el Controlador, se utiliza la anotación `@Valid` para disparar el chequeo.

### Tipos Primitivos vs Wrappers (`int` vs `Integer`)
Al validar números en un formulario web, la elección del tipo de dato es vital:
* **`int` (Primitivo):** No puede ser nulo. Si un usuario envía un formulario con el campo numérico vacío, Spring intenta convertir un `String` vacío a `int`, falla silenciosamente o lanza un error de "Type Mismatch" antes de siquiera llegar a tus validaciones.
* **`Integer` (Wrapper):** Es un objeto y **puede ser `null`**. Si el campo llega vacío, se convierte en `null`. Esto nos permite usar la anotación `@NotNull` para capturar el error de forma elegante y devolverle un mensaje amigable al usuario. **Siempre usa Wrappers al trabajar con formularios web.**

### El objeto `BindingResult` y los Mensajes Personalizados
El `BindingResult` es un objeto de Spring que contiene el resultado de la validación. Siempre debe declararse en los parámetros del Controlador **inmediatamente después** del atributo `@Valid`.
Si imprimes el `BindingResult` en la consola (`System.out.println(bindingResult);`), podrás ver los **códigos de error internos** que genera Spring cuando una validación falla (ej. `typeMismatch.student.freePasses`). 

**Custom Messages:** Tomando esos códigos exactos de la consola, podemos crear un archivo llamado `messages.properties` en `src/main/resources` y anular el mensaje genérico de Spring:
`typeMismatch.student.freePasses=El valor debe ser un número entero`

---

## 4. Expresiones Regulares (Regular Expressions / Regex)

Son secuencias de caracteres que forman un patrón de búsqueda. Se utilizan en conjunto con la anotación `@Pattern` para validar que un campo de texto cumpla con un formato estricto (ej. códigos postales, emails, patentes).

| Símbolo | Significado |
| :--- | :--- |
| `^` | Inicio de la cadena. |
| `$` | Fin de la cadena. |
| `[a-zA-Z]` | Rango: Cualquier letra desde la "a" hasta la "z" (mayúscula o minúscula). |
| `\d` | Representa cualquier dígito numérico (0-9). |
| `.` | Comodín: Cualquier carácter único. |
| `*` | El elemento anterior puede aparecer 0 o más veces. |
| `+` | El elemento anterior debe aparecer 1 o más veces. |
| `?` | El elemento anterior es opcional (aparece 0 o 1 vez). |
| `{n}` | El elemento anterior debe aparecer exactamente "n" veces. |

---

## 5. El problema de los Espacios en Blanco (`@InitBinder`)

Un usuario malicioso puede evadir la validación `@NotNull` simplemente escribiendo puros espacios en blanco en un campo de texto. Para evitarlo, pre-procesamos las peticiones web en el Controlador utilizando `@InitBinder`.
Registramos un `StringTrimmerEditor(true)`, el cual recorta los espacios en blanco sobrantes. Si el `String` queda completamente vacío tras el recorte, lo convierte en `null`, logrando que la anotación `@NotNull` haga su trabajo correctamente.

---

## 6. Validaciones Personalizadas (Custom Validations)

Cuando las validaciones estándar de Java (como tamaño o nulos) no alcanzan para nuestras reglas de negocio, podemos crear nuestras propias anotaciones desde cero. El flujo consta de dos partes:

### Parte 1: Creación de la Anotación (`@interface`)
Se crea una interfaz con la palabra clave `@interface` y se decora con meta-anotaciones:
* `@Constraint(validatedBy = MiClaseValidadora.class)`: Delega la lógica real a una clase externa.
* `@Target({ElementType.METHOD, ElementType.FIELD})`: Define que esta anotación solo puede usarse sobre métodos o atributos de una clase.
* `@Retention(RetentionPolicy.RUNTIME)`: Indica que la anotación debe conservarse en el archivo compilado y estar disponible en la memoria (JVM) mientras el programa se ejecuta.

**Métodos obligatorios de la interfaz:**
La API de validación de Java exige que la interfaz contenga dos métodos específicos, aunque no los usemos directamente:
1. `Class<?>[] groups() default {};`: Permite agrupar validaciones (ej. aplicar ciertas validaciones solo cuando se crea un usuario, y otras solo cuando se actualiza).
2. `Class<? extends Payload>[] payload() default {};`: Permite transportar información adicional sobre el error, como asignar niveles de severidad (ej. Error Crítico, Advertencia).

### Parte 2: Creación del ConstraintValidator
Se crea una clase Java normal que implementa la interfaz `ConstraintValidator<MiAnotacion, TipoDeDato>`.
Esta clase nos obliga a sobrescribir el método `isValid(String theData, ConstraintValidatorContext context)`. Es dentro de este método donde escribimos nuestra lógica de negocio (`if/else`) y retornamos `true` (si pasó la prueba) o `false` (si falló y debe lanzar el error).

---

## 7. Glosario de Anotaciones (Sección 6)

| Anotación | Nivel | Descripción |
| :--- | :--- | :--- |
| `@Controller` | Clase | Variante de `@Component`. Marca la clase como un controlador de Spring MVC. Sus métodos retornan el nombre de las vistas (archivos HTML), no datos JSON puros. |
| `@GetMapping` / `@PostMapping` | Método | Mapean peticiones HTTP GET y POST a métodos específicos del controlador. |
| `@RequestParam` | Parámetro | Extrae y enlaza parámetros de la URL (query parameters) o datos crudos de un formulario tradicional hacia una variable del método. |
| `@ModelAttribute` | Parámetro | Enlaza los datos de un formulario Thymeleaf (`th:object`) directamente a un POJO de Java de forma automática. |
| `@Valid` | Parámetro | Le indica a Spring que debe inspeccionar el objeto y ejecutar todas las reglas de validación antes de continuar con el código del método. |
| `@NotNull` | Atributo | Regla de validación: El campo no puede ser nulo. |
| `@Size(min, max)` | Atributo | Regla de validación: Define la longitud mínima y/o máxima de un `String` o una colección. |
| `@Min(value)` / `@Max(value)` | Atributo | Regla de validación: Define el valor numérico mínimo y máximo permitido. |
| `@Pattern(regexp)` | Atributo | Regla de validación: Fuerza a que el campo coincida con una Expresión Regular. |
| `@InitBinder` | Método | Anotación de Spring MVC. El método decorado con ella se ejecuta antes que cualquier otro endpoint, permitiendo pre-procesar, formatear o limpiar los datos que llegan (ej. recortar espacios con `StringTrimmerEditor`). |