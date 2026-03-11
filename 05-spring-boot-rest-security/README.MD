# Curso Spring Boot - Sección 5: Spring Security

Este documento es una referencia detallada sobre la implementación de seguridad en APIs REST utilizando **Spring Security**. Cubre desde la configuración básica en memoria hasta la autenticación avanzada con base de datos, encriptación de contraseñas y la integración profesional mediante JPA/Hibernate.

---

## 1. Conceptos Básicos de Spring Security

Al agregar la dependencia `spring-boot-starter-security` al `pom.xml`, Spring Boot aplica un comportamiento por defecto:
* Protege absolutamente todos los endpoints de la aplicación.
* Configura un usuario por defecto llamado `user`.
* Genera una contraseña aleatoria en la consola cada vez que arranca la aplicación.
* Habilita la autenticación *Basic Auth* y genera un formulario de login por defecto.

---

## 2. Autenticación en Memoria (In-Memory Authentication)

Para evitar la contraseña aleatoria, podemos definir nuestros propios usuarios en memoria. Esto se utiliza principalmente para pruebas y pruebas de concepto (PoC).
* Se crea una clase de configuración anotada con `@Configuration`.
* Se expone un `@Bean` de tipo `InMemoryUserDetailsManager`.
* Se utiliza el patrón Builder (`User.builder()`) para crear objetos `UserDetails` con un nombre de usuario, una contraseña (en texto plano usando el prefijo `{noop}`) y un rol específico (ej. `EMPLOYEE`, `MANAGER`).

---

## 3. Autorización y Control de Acceso basado en Roles (RBAC)

No todos los usuarios deben tener acceso a las mismas operaciones. Spring Security permite restringir endpoints específicos según el método HTTP y el rol del usuario.
* Se define un `@Bean` de tipo `SecurityFilterChain`.
* Se utiliza `http.authorizeHttpRequests()` para declarar las reglas.
* Mediante `.requestMatchers(HttpMethod, "ruta").hasRole("ROL")`, se define quién puede ejecutar qué.
  * *Ejemplo:* `GET` para `EMPLOYEE`, `POST/PUT` para `MANAGER`, `DELETE` para `ADMIN`.
* **Regla de oro:** Siempre incluir `.anyRequest().authenticated()` al final para garantizar que cualquier endpoint no mapeado requiera al menos estar logueado.

---

## 4. ⚠️ IMPORTANTE: El problema de la Barra Final (Trailing Slash)

En **Spring Boot 3 / Spring Security 6**, el enrutamiento y la coincidencia de URLs son **estrictamente literales**.

* Si tu configuración de seguridad protege el endpoint `"/api/employees"`, y el cliente (Postman/Insomnia) envía una petición a `"/api/employees/"` (con una barra `/` al final), **Spring Security considerará que son dos rutas distintas**.
* Al no encontrar la ruta con la barra final en las reglas de autorización, la petición será bloqueada por defecto, devolviendo un error **403 Forbidden**.
* **Solución:** Asegurarse siempre de que la URL en el cliente HTTP no contenga barras diagonales innecesarias al final y coincida exactamente con lo escrito en el `requestMatchers`.

---

## 5. Autenticación con Base de Datos (JDBC Authentication)

Para entornos de producción, los usuarios se almacenan en una base de datos.
* **Esquema por defecto:** Spring Security reconoce automáticamente tablas llamadas `users` y `authorities` si tienen la estructura exacta que el framework espera. Se habilita devolviendo un `@Bean` de tipo `JdbcUserDetailsManager(dataSource)`.
* **Esquema personalizado (Tablas Custom):** Si la base de datos tiene tablas con nombres propios (ej. `members` y `roles`), se debe indicar a Spring cómo buscar los datos mediante sentencias SQL personalizadas utilizando:
  * `.setUsersByUsernameQuery("SELECT user_id, pw, active FROM members WHERE user_id=?")`
  * `.setAuthoritiesByUsernameQuery("SELECT user_id, role FROM roles WHERE user_id=?")`

### Encriptación de Contraseñas (BCrypt)
Almacenar contraseñas en texto plano es una pésima práctica. Spring Security soporta algoritmos de hashing seguros como BCrypt.
* El prefijo en la base de datos pasa de `{noop}` a `{bcrypt}`.
* La columna de la contraseña en la BD debe ser de tipo `char(68)` para alojar exactamente el tamaño del hash de BCrypt más el prefijo.

---

## 6. Implementación Avanzada: Spring Security con JPA e Hibernate

> El enfoque más profesional no utiliza sentencias SQL crudas ni la clase `JdbcUserDetailsManager`, sino que integra Spring Security directamente con el ORM (Hibernate) mediante JPA. 

>Esta arquitectura permite manejar entidades complejas (usuarios con emails, avatares, fechas) y gestionar la autenticación y autorización puramente en Java.

### Flujo de Archivos y Arquitectura JPA-Security

El flujo para conectar la base de datos de seguridad a través de JPA sigue este camino exacto:

1. **Entidades JPA (Entity):** Se crean las clases `@Entity User` y `@Entity Role` mapeadas a las tablas de la base de datos.
2. **Capa DAO / Repositorio (`UserDao` y `UserDaoImpl`):** Se utiliza el `EntityManager` para realizar las consultas a la base de datos usando JPQL (ej. buscar el usuario por su nombre).
3. **Capa de Servicio (El "Traductor"):** * Se crea un `UserService` que extiende de la interfaz nativa de Spring Security llamada `UserDetailsService`.
   * Se sobrescribe el método `loadUserByUsername(String userName)`.
   * Este método busca al usuario en el DAO y "traduce" la entidad propia `User` a un objeto `UserDetails` que Spring Security entiende. 
   * También se mapean los roles de la base de datos a una colección de `GrantedAuthority`.
4. **Configuración de Seguridad (`DemoSecurityConfig`):**
   * En lugar del `JdbcUserDetailsManager`, se declara un `@Bean` de tipo `DaoAuthenticationProvider`.
   * A este proveedor se le inyecta nuestro `UserService` personalizado y un encriptador de contraseñas (`BCryptPasswordEncoder`).
   * Spring Security ahora sabe que, para hacer un login, debe usar a nuestro proveedor, que a su vez usa nuestro servicio, el cual usa JPA para ir a la base de datos.

---

## 7. Glosario de Anotaciones y Clases Clave (Sección 5)

| Elemento / Anotación | Tipo | Descripción |
| :--- | :--- | :--- |
| `@Configuration` | Anotación | Indica que una clase contiene métodos `@Bean` y sirve para configurar el contexto de la aplicación (en este caso, la seguridad). |
| `@Bean` | Anotación | Indica que el método devuelve un objeto que debe ser administrado por el contenedor de Spring. |
| `SecurityFilterChain` | Interfaz | Bean utilizado para definir todas las reglas de filtrado HTTP, autorización de rutas y métodos de login/logout. |
| `InMemoryUserDetailsManager` | Clase | Implementación que almacena usuarios y credenciales en la memoria RAM del servidor. |
| `JdbcUserDetailsManager` | Clase | Implementación que se conecta a una base de datos mediante JDBC para autenticar usuarios mediante consultas SQL. |
| `UserDetailsService` | Interfaz | Interfaz core de Spring Security que se debe implementar cuando se desea integrar un proveedor de usuarios personalizado (como JPA). |
| `DaoAuthenticationProvider` | Clase | Proveedor de autenticación de Spring Security que recupera los detalles del usuario a través de un `UserDetailsService`. |
| `BCryptPasswordEncoder` | Clase | Bean que implementa el algoritmo de hashing BCrypt para encriptar y validar contraseñas de forma segura. |