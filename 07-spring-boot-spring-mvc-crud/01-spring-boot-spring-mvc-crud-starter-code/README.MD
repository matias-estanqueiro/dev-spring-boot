# Curso Spring Boot - Sección 7: Spring MVC CRUD

Este documento resume la integración de **Spring MVC** con **Spring Data JPA** para construir una aplicación web Full-Stack con capacidades CRUD (Create, Read, Update, Delete). En esta sección, conectamos una interfaz gráfica construida con Thymeleaf a una base de datos relacional mediante una arquitectura limpia de tres capas.

---

## 1. Arquitectura de la Aplicación

La aplicación sigue el flujo de trabajo estándar de la industria:
1. **Vista (Thymeleaf):** Archivos `.html` que muestran los datos y capturan la entrada del usuario mediante formularios.
2. **Controlador (`@Controller`):** Recibe las peticiones HTTP de la vista, delega la lógica de negocio al Servicio, y retorna el nombre de la vista a renderizar o una redirección.
3. **Servicio (`@Service`):** Capa intermediaria (Service Facade) que maneja la lógica de negocio transaccional.
4. **Repositorio (`Spring Data JPA`):** Se encarga de la comunicación directa con la base de datos de forma automática, sin necesidad de escribir sentencias SQL.

---

## 2. El Patrón Post/Redirect/Get (PRG)

Un concepto crucial introducido en esta sección para manejar envíos de formularios.

* **El Problema:** Si un usuario envía un formulario (`POST`) para guardar un empleado y el controlador simplemente devuelve la vista HTML (`return "employee-list";`), la URL en el navegador seguirá siendo `/save`. Si el usuario presiona "F5" (Actualizar), el navegador **reenviará el formulario POST**, creando un registro duplicado en la base de datos.
* **La Solución (PRG):** Después de procesar un formulario POST con éxito, el controlador **nunca** debe devolver una vista directamente. En su lugar, debe devolver una instrucción de redirección (`return "redirect:/employees/list";`). Esto fuerza al navegador a hacer una nueva petición `GET` limpia, actualizando la URL y evitando envíos duplicados si el usuario recarga la página.

---

## 3. Flujo de Actualización (Update)

Para actualizar un registro existente, se requieren dos pasos (dos métodos en el controlador):
1. **Mostrar el Formulario (GET):** Se recibe el `id` del empleado a través de la URL (ej. `/showFormForUpdate?employeeId=5`). El controlador usa el Servicio para buscar ese empleado en la base de datos y lo agrega al `Model`. Thymeleaf usa ese objeto para rellenar (`pre-populate`) los campos del formulario.
2. **Guardar los Cambios (POST):** El formulario incluye un campo oculto (`<input type="hidden" th:field="*{id}" />`). Al enviar el formulario, Spring reconoce que el objeto ya tiene un ID asignado, por lo que Spring Data JPA ejecutará un `UPDATE` en la base de datos en lugar de un `INSERT`.

---

## 4. Glosario de Atributos Thymeleaf (`th:`)

En esta sección utilizamos Thymeleaf no solo para renderizar, sino para enlazar acciones y crear URLs dinámicas.

| Atributo | Explicación y Uso en CRUD |
| :--- | :--- |
| `th:each` | **Iteración.** Se usa para recorrer la lista de empleados enviada desde el controlador y generar una fila `<tr>` de tabla por cada uno. Ej: `th:each="tempEmployee : ${employees}"`. |
| `th:text` | **Impresión.** Muestra el valor de una propiedad en la pantalla. Ej: `th:text="${tempEmployee.firstName}"`. |
| `th:action` | **Envío de formulario.** Define la ruta a la que viajarán los datos del `POST`. Ej: `th:action="@{/employees/save}"`. |
| `th:object` | **Declaración de Modelo.** Indica qué objeto del `Model` va a representar este formulario. Ej: `th:object="${employee}"`. |
| `th:field` | **Data Binding.** Vincula un `<input>` a una propiedad del objeto. Además, pre-rellena el campo si el objeto ya tiene datos (vital para el Update). Ej: `th:field="*{lastName}"`. |
| `th:href` | **Enlaces Dinámicos.** Genera URLs seguras para botones. Permite agregar parámetros de consulta (Query Params) fácilmente. Ej: `th:href="@{/employees/showFormForUpdate(employeeId=${tempEmployee.id})}"`. |

---

## 5. Glosario de Anotaciones Clave (Spring MVC)

| Anotación | Propósito en el CRUD |
| :--- | :--- |
| `@Controller` | Marca la clase para que Spring la registre como un bean capaz de manejar peticiones web y retornar vistas Thymeleaf. |
| `@RequestMapping` | A nivel de clase, define una ruta base para todos los métodos (ej. `/employees`). |
| `@GetMapping` | Mapea peticiones HTTP GET. Se usa para mostrar páginas (listar, mostrar formulario de alta/edición). |
| `@PostMapping` | Mapea peticiones HTTP POST. Se usa para procesar datos confidenciales o envíos de formularios (guardar/actualizar empleado). |
| `@RequestParam` | Extrae parámetros que viajan en la URL (Query Parameters). Se usó para capturar el ID del empleado en los botones de "Actualizar" y "Eliminar". Ej: `(@RequestParam("employeeId") int theId)`. |
| `@ModelAttribute` | Captura todos los datos del formulario HTML (`th:object`) y los agrupa/mapea directamente en un objeto Java listo para ser guardado. |
| `@Autowired` | (Opcional en constructores modernos). Inyecta la dependencia del `EmployeeService` en el Controlador. |