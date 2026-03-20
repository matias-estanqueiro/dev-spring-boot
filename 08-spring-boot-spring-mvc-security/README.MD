# Curso Spring Boot - Sección 8: Spring MVC Security

Este documento resume la integración de **Spring Security** con aplicaciones web basadas en **Spring MVC y Thymeleaf**. En esta sección, pasamos de proteger simples APIs REST a blindar una interfaz gráfica completa, controlando qué ven y a dónde pueden acceder los usuarios según sus roles.

---

## 1. Integración de Seguridad y Vistas (Login y Logout Custom)

Por defecto, Spring Security provee un formulario de login genérico. En esta sección aprendimos a reemplazarlo por una vista HTML personalizada integrada estéticamente con nuestra aplicación.

* **Login Personalizado:** Se configura en el `SecurityFilterChain` usando `.formLogin(form -> form.loginPage("/showMyLoginPage").loginProcessingUrl("/authenticateTheUser"))`. Spring Security se encarga automáticamente de procesar el POST, validar las credenciales y crear la sesión; nosotros solo proveemos el HTML.
* **Logout Personalizado:** Se habilita mediante `.logout(logout -> logout.permitAll())`. En Thymeleaf, basta con enviar un formulario POST a la ruta `/logout`. Spring intercepta esta petición, invalida la sesión del usuario, limpia las cookies y lo redirige al login.

---

## 2. Control de Acceso Basado en Roles (RBAC) en Rutas

Utilizamos el bloque `authorizeHttpRequests` para definir qué roles pueden acceder a qué rutas (URLs) de nuestra aplicación.
* Los usuarios normales (`EMPLOYEE`) pueden ver la lista general.
* Los gerentes (`MANAGER`) pueden acceder a rutas de actualización y creación.
* Los administradores (`ADMIN`) tienen acceso exclusivo a zonas críticas (ej. paneles de sistemas).

---

## 3. Manejo de Excepciones: Acceso Denegado (403)

Cuando un usuario autenticado intenta ingresar a una URL para la cual no tiene el rol necesario, Spring Security lanza una excepción y devuelve un error HTTP 403 (Forbidden).
* **Configuración:** Capturamos este error usando `.exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))`.
* **Regla Crítica (Prevención de Bucle Infinito):** La ruta `/access-denied` **debe ser de acceso público** (`.permitAll()`). Si se protege por error, Spring entrará en un bucle infinito de redirecciones intentando mostrar una página de error a la que el usuario tampoco tiene permiso de ver, resultando en un colapso del servidor (`StackOverflowError`).

---

## 4. Thymeleaf Extras for Spring Security

Para que la interfaz de usuario sea verdaderamente dinámica y segura, agregamos la dependencia `thymeleaf-extras-springsecurity6`. Esto nos permite alterar el DOM (HTML) en tiempo real según el contexto de seguridad.

### Atributos Thymeleaf Security (`sec:`)

| Atributo / Expresión | Propósito / Explicación |
| :--- | :--- |
| `sec:authorize="hasRole('MANAGER')"` | **Renderizado Condicional:** Evalúa si el usuario actual tiene el rol especificado. Si es `false`, la etiqueta HTML (y todo su contenido) se elimina completamente del código fuente antes de enviarlo al navegador. Ideal para ocultar botones de "Actualizar" o "Eliminar". |
| `sec:authorize="isAuthenticated()"` | Muestra el contenido solo si el usuario ha iniciado sesión. |
| `sec:authentication="principal.username"`| **Extracción de Datos:** Obtiene información directamente del token de sesión (el objeto `Principal` de Java). Se usa habitualmente para mostrar mensajes como "Bienvenido, *{usuario}*" en la barra de navegación. |
| `sec:authentication="principal.authorities"`| Imprime en pantalla la lista de roles/autoridades que posee el usuario actual. |

---

## 5. Autenticación contra Base de Datos (JDBC)

Abandonamos los usuarios en memoria para conectarnos a tablas reales en MS SQL Server / MySQL.
* Se configuraron esquemas de tablas personalizadas (ej. `members` y `roles`).
* Se instruyó a Spring Security mediante consultas SQL explícitas (`setUsersByUsernameQuery` y `setAuthoritiesByUsernameQuery`) sobre cómo buscar a los usuarios.
* Todas las contraseñas se almacenan y validan utilizando el algoritmo de encriptación **BCrypt**, garantizando que ninguna credencial quede expuesta en texto plano.

---

## 6. Glosario de Métodos Clave (Configuración de Seguridad)

| Clase / Método | Propósito en Spring MVC Security |
| :--- | :--- |
| `SecurityFilterChain` | Bean central que define toda la cadena de filtros de seguridad HTTP para la aplicación web. |
| `.loginPage(String)` | Indica la ruta del Controlador que devuelve el archivo HTML del formulario de inicio de sesión personalizado. |
| `.loginProcessingUrl(String)` | Define la ruta secreta a la que el formulario HTML debe hacer el `POST`. Spring Security intercepta esta ruta automáticamente; no necesitamos crear un método en el Controlador para ella. |
| `.exceptionHandling()` | Bloque para personalizar la respuesta ante errores de seguridad (como el 403 Forbidden). |
| `.accessDeniedPage(String)` | Define a qué ruta redirigir al usuario cuando intenta realizar una acción no autorizada. |