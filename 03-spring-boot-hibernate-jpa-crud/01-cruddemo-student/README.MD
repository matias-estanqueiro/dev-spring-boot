# Sección 3: Acceso a Datos con Hibernate y JPA (CRUD)

Este documento resume los conceptos fundamentales para la persistencia de datos en bases de datos relacionales utilizando Spring Boot, Hibernate (como proveedor de JPA) y el patrón de diseño DAO (Data Access Object).

## 1. Patrón Arquitectónico: Flujo de Creación de Clases

Para mantener un acoplamiento débil (loose coupling) y respetar los principios arquitectónicos, la interacción con la base de datos sigue un flujo estricto de tres pasos:

1. **La Interfaz (`StudentDAO`):** Se define un contrato que expone únicamente *qué* operaciones se pueden hacer (ej. `save()`, `findById()`), sin importar *cómo* se hacen ni qué base de datos hay por detrás.
2. **La Implementación (`StudentDAOImpl`):** Una clase concreta que implementa la interfaz. Aquí se inyecta el `EntityManager` y se escribe el código específico de JPA/Hibernate. Se anota con `@Repository`.
3. **La Inyección (En el Service o Main):** Cuando otra clase necesita acceder a los datos, **siempre se inyecta la Interfaz, nunca la implementación**. Spring IoC buscará la clase que implementa esa interfaz y la conectará automáticamente.

## 2. El Gestor de Entidades: `EntityManager`

El `EntityManager` es el componente principal de JPA. Actúa como un puente entre tus objetos Java (Entities) y la base de datos relacional. Internamente gestiona las conexiones JDBC y el "Contexto de Persistencia" (la memoria caché de los objetos rastreados).

### Métodos principales del `EntityManager`

| Método | Operación CRUD | Descripción |
| :--- | :--- | :--- |
| `persist(entity)` | **C**reate (Insert) | Toma un objeto Java nuevo y lo guarda en la base de datos. Modifica el objeto original asignándole el ID generado. |
| `find(Class, id)` | **R**ead (Select) | Busca un registro por su Clave Primaria (ID). Si lo encuentra, devuelve el objeto; si no, devuelve `null`. |
| `merge(entity)` | **U**pdate (Update) | Toma un objeto modificado y sincroniza sus cambios con el registro correspondiente en la base de datos. |
| `remove(entity)` | **D**elete (Delete) | Elimina de la base de datos el registro asociado a la entidad proporcionada. |
| `createQuery(jpql, Class)`| **R**ead (Select) | Permite ejecutar consultas complejas o personalizadas utilizando el lenguaje JPQL. |

## 3. Gestión de Transacciones: `@Transactional`

La anotación `@Transactional` le indica a Spring que maneje la transacción de base de datos automáticamente.

* **¿Qué hace?** Envuelve el método para abrir una transacción (`BEGIN`) antes de que se ejecute tu código. Si el método termina con éxito, guarda los cambios (`COMMIT`). Si ocurre un error o excepción, deshace todo (`ROLLBACK`) para no corromper la base de datos.
* **¿Cuándo se usa?** Es obligatoria en métodos que modifican la base de datos (Insert, Update, Delete). No es estrictamente necesaria para consultas de solo lectura (Select).

## 4. Java Persistence Query Language (JPQL)

JPQL es un lenguaje de consultas similar a SQL, pero **consulta Clases y Atributos de Java (Entidades), NO Tablas y Columnas de la base de datos.** Esto hace que las consultas sean independientes del motor de base de datos (puedes cambiar de MySQL a SQL Server sin reescribir consultas).

### Equivalencias: SQL Nativo vs JPQL

| Operación | Consulta SQL Nativa (Apunta a Tablas) | Consulta JPQL (Apunta a Clases Java) |
| :--- | :--- | :--- |
| **SELECT ALL** | `SELECT * FROM student_table` | `SELECT s FROM Student s` |
| **WHERE** | `SELECT * FROM student_table WHERE last_name = 'Doe'` | `SELECT s FROM Student s WHERE s.lastName = 'Doe'` |
| **LIKE** | `SELECT * FROM student_table WHERE first_name LIKE '%a%'` | `SELECT s FROM Student s WHERE s.firstName LIKE '%a%'` |
| **AND / OR** | `SELECT * FROM student_table WHERE last_name = 'Doe' OR first_name = 'John'` | `SELECT s FROM Student s WHERE s.lastName = 'Doe' OR s.firstName = 'John'` |
| **ORDER BY** | `SELECT * FROM student_table ORDER BY last_name DESC` | `SELECT s FROM Student s ORDER BY s.lastName DESC` |
| **UPDATE** | `UPDATE student_table SET last_name = 'Smith'` | `UPDATE Student SET lastName = 'Smith'` |
| **DELETE** | `DELETE FROM student_table WHERE id = 1` | `DELETE FROM Student WHERE id = 1` |

*(Nota: En JPQL, debes asignar un alias obligatoriamente, por ejemplo, `s` para `Student`).*

## 5. Glosario de Anotaciones de la Sección

| Anotación | Nivel | Descripción y Uso |
| :--- | :--- | :--- |
| `@Entity` | Clase | Marca una clase Java simple como una Entidad JPA que se mapeará a una tabla de base de datos. |
| `@Table(name="x")` | Clase | (Opcional) Especifica el nombre de la tabla en la base de datos si difiere del nombre de la clase Java. |
| `@Id` | Atributo | Marca un campo específico como la Clave Primaria (Primary Key) de la entidad. |
| `@GeneratedValue(strategy=...)` | Atributo | Indica cómo se generará la Clave Primaria. `GenerationType.IDENTITY` delega la creación del ID al motor de la base de datos (autoincremental). |
| `@Column(name="x")` | Atributo | (Opcional) Mapea el atributo Java a una columna específica. Si se omite, JPA asume que la columna se llama igual que el atributo. |
| `@Repository` | Clase | Estereotipo de Spring. Marca la clase DAO para que sea registrada como un Bean y habilita la traducción de excepciones de base de datos. |
| `@Transactional` | Método / Clase | Delega a Spring la apertura, el commit y el rollback automático de la transacción. |

## 6. Ejecución por Consola: `CommandLineRunner`

En esta sección del curso no estamos construyendo una API web (no hay `@RestController`), por lo que la aplicación arranca e inmediatamente se apaga. Para poder probar nuestro código DAO, utilizamos `CommandLineRunner`.

* **¿Qué es?** Es una interfaz funcional de Spring Boot que se ejecuta automáticamente una sola vez justo después de que el *Application Context* (el contenedor de Beans) ha terminado de inicializarse.
* **Uso en el curso:** Se inyecta como un `@Bean` en la clase principal (`CruddemoApplication`) y dentro de él escribimos los "scripts" de prueba para instanciar objetos `Student` y pasárselos a los métodos de nuestro DAO.

## 7. Sincronización Automática de Esquemas (`ddl-auto`)

Hibernate tiene una característica poderosa llamada **Schema Generation**. Puede leer tus clases anotadas con `@Entity` y ejecutar sentencias DDL (Data Definition Language) por detrás para crear o modificar las tablas físicas en tu base de datos automáticamente.

Esto se configura en el archivo `application.properties` mediante la propiedad `spring.jpa.hibernate.ddl-auto`:

| Valor | Comportamiento | Entorno de uso |
| :--- | :--- | :--- |
| `create-drop` | Crea el esquema al iniciar y lo destruye completamente al apagar la aplicación. | Pruebas automatizadas (Testing). |
| `create` | Borra las tablas existentes y las vuelve a crear vacías en cada reinicio. | Desarrollo temprano. |
| `update` | *(El usado en el curso)*. Compara tus clases Java con las tablas de la BD y agrega las columnas o tablas faltantes sin borrar los datos existentes. | Desarrollo local. |
| `none` | Desactiva completamente esta función. | **Producción** (Mandatorio). |

## 8. Estrategias de Generación de Identificadores

Cuando utilizamos `@Id` junto con `@GeneratedValue`, le indicamos a JPA cómo queremos que se generen las claves primarias numéricas. El curso se centra en una, pero existen múltiples estrategias (`GenerationType`):

* **`IDENTITY`:** (La utilizada en el curso). Delega la creación del ID completamente al motor de la base de datos utilizando columnas autoincrementales (como `IDENTITY` en MS SQL Server o `AUTO_INCREMENT` en MySQL).
* **`SEQUENCE`:** Utiliza un objeto de "secuencia" propio de la base de datos para generar los números. Es el estándar de alto rendimiento para bases de datos como Oracle o PostgreSQL.
* **`AUTO`:** Es el valor por defecto. Le cede a Hibernate la decisión de elegir la mejor estrategia (`IDENTITY`, `SEQUENCE` o `TABLE`) basándose en el "Dialecto" del motor de base de datos que estés utilizando.