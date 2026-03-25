# Sección 9: Mapeos Avanzados de Hibernate y JPA

Este documento es una guía definitiva y un "Cheat Sheet" detallado sobre cómo manejar relaciones complejas en bases de datos relacionales utilizando Spring Boot, JPA y Hibernate. 

A diferencia de un CRUD básico, aquí entramos en la arquitectura profunda de cómo los objetos Java se comunican entre sí y cómo Hibernate traduce esa comunicación a SQL de manera eficiente.

---

## 1. Conceptos Core de JPA y Hibernate

### 1.1. Ciclo de Vida de una Entidad (Entity Lifecycle)
Cada objeto `@Entity` en Java pasa por diferentes estados en relación con la base de datos (manejados por el `EntityManager`):
* **Transient (Nuevo):** El objeto fue creado con `new`, pero Hibernate no sabe que existe. No tiene ID y no está en la base de datos.
* **Managed / Persistent (Manejado):** El objeto está guardado en la BD y atado a una sesión (transacción) activa. Si usas un *setter* en este estado, Hibernate hará un `UPDATE` automático al cerrar la transacción.
* **Detached (Desconectado):** El objeto tiene un ID y existe en la BD, pero la conexión (sesión) que lo trajo ya se cerró. Hibernate ya no lo rastrea.
* **Removed (Eliminado):** El objeto está marcado para ser borrado (`DELETE`) de la base de datos al finalizar la transacción.

### 1.2. Tipos de Cascada (Cascade Types)
La cascada responde a la pregunta: *"Si le hago algo a la entidad Padre, ¿debería aplicarse lo mismo a sus entidades Hijas?"*
* `CascadeType.PERSIST`: Si guardo al padre, guarda a los hijos automáticamente.
* `CascadeType.MERGE`: Si actualizo al padre, actualiza a los hijos.
* `CascadeType.REMOVE`: Si borro al padre, borra a los hijos (Ej: Borrar un curso borra sus reseñas). **CUIDADO**: No usar si los hijos tienen vida independiente (Ej: Borrar un curso NO debería borrar a los estudiantes).
* `CascadeType.REFRESH`: Si recargo al padre desde la BD, recarga a los hijos.
* `CascadeType.DETACH`: Si desconecto al padre, desconecta a los hijos.
* `CascadeType.ALL`: Aplica todos los anteriores. Útil para relaciones estrechas (Instructor -> InstructorDetail).

---

## 2. Fetch Types: LAZY vs EAGER Loading

Este es el concepto más crítico para el rendimiento de una aplicación a nivel empresarial. Define **cuándo** se traen los datos relacionados desde la base de datos.

### 2.1. EAGER Loading
* **Definición:** Trae TODO inmediatamente. Si buscas un Instructor, hace un `JOIN` y trae todos sus cursos en ese mismo instante, los necesites o no.
* **Cuándo usarlo:** Casi NUNCA a nivel global. Solo en relaciones `@OneToOne` estrictas o cuando estás 100% seguro de que SIEMPRE vas a necesitar esa información.
* **Peligro:** Causa colapsos de memoria. Si la jerarquía es profunda, terminas descargando media base de datos por un solo registro.

### 2.2. LAZY Loading
* **Definición:** Trae a demanda. Busca al Instructor, pero deja su lista de Cursos vacía (como un "Proxy" o fantasma). Los cursos solo se traen de la BD (con un nuevo `SELECT`) cuando tu código llama a `getCourses()`.
* **Cuándo usarlo:** **SIEMPRE** es la mejor práctica por defecto para colecciones (Listas). Protege la memoria del servidor.
* **Peligro:** El `LazyInitializationException` (ver sección FAQ).

### 2.3. Defaults de JPA
* `@OneToOne` y `@ManyToOne` ➔ **EAGER** por defecto.
* `@OneToMany` y `@ManyToMany` ➔ **LAZY** por defecto.

### 2.4. `JOIN FETCH`
Si la regla de la industria es usar siempre `LAZY`, ¿cómo evitamos hacer múltiples consultas SQL (Problema N+1) cuando realmente *sí* necesitamos mostrar los datos relacionados? La respuesta es el **`JOIN FETCH`**.

* **¿Qué es?** Es una instrucción exclusiva de JPQL. Le dice a Hibernate: *"Sé que esta relación es LAZY, pero por esta única vez, ignora esa regla y trae los datos relacionados en esta misma consulta SQL usando un JOIN"*.
* **¿Cómo se usa?** Se escribe dentro de la consulta JPQL, agregando la palabra `FETCH` después de un `JOIN`:
  ```sql
  SELECT i FROM Instructor i JOIN FETCH i.courses WHERE i.id = :id
  ```
* **Ventajas:**
  1. **Soluciona el Problema N+1:** En lugar de hacer 1 consulta para el instructor y N consultas para sus cursos, trae todo en **1 sola consulta SQL** altamente optimizada.
  2. **Previene el `LazyInitializationException`:** Como los datos ya vienen "hidratados" (cargados) desde el momento de la consulta, puedes cerrar la sesión de la base de datos y seguir usando `.getCourses()` sin que la aplicación explote.
  3. **Mantiene la arquitectura limpia:** Te permite mantener el `@OneToMany(fetch = FetchType.LAZY)` global en tu entidad, usando el EAGER de forma quirúrgica solo cuando la pantalla lo requiere.

---

## 3. Relaciones y Mapeos de Base de Datos

### 3.1. Arquitectura: Owning Side vs Inverse Side
En relaciones bidireccionales, JPA necesita saber quién manda (Owning Side).
* **Owning Side (Lado Propietario):** Es la entidad cuya tabla en la base de datos **físicamente contiene la columna de la llave foránea (Foreign Key)**. Esta entidad usa `@JoinColumn` o `@JoinTable`.
* **Inverse / Non-Owning Side (Lado Inverso):** Es la entidad que NO tiene la Foreign Key física en su tabla, pero quiere saber sobre la relación. Esta entidad **SIEMPRE usa el atributo `mappedBy="nombreAtributoEnElLadoPropietario"`**.

**💡 Reglas para elegir cuál es cuál:**
1. **Relaciones 1 a Muchos (`@OneToMany` / `@ManyToOne`):** El *Owning Side* **siempre** es el lado "Muchos" (`@ManyToOne`), porque en las bases de datos relacionales, la llave foránea siempre se guarda en la tabla hija (Ej: La tabla `course` tiene la columna `instructor_id`).
2. **Relaciones 1 a 1 (`@OneToOne`):** El *Owning Side* es la tabla que elegiste para que guarde la llave foránea física (Ej: Si `instructor` tiene la columna `instructor_detail_id`, entonces `Instructor` es el Owning Side).
3. **Relaciones Muchos a Muchos (`@ManyToMany`):** En base de datos usan una tabla intermedia (`JOIN TABLE`), así que técnicamente a nivel SQL ninguna es dueña de la otra. A nivel de código Java, tú eliges arbitrariamente al dueño. Por lo general, se elige a la entidad "Padre" o principal del contexto de tu negocio (Ej: Elegimos que `Course` sea el dueño sobre `Student` porque administramos cursos).

### 3.2. Métodos de Conveniencia (Convenience Methods)
En relaciones bidireccionales (ej. Instructor <-> Course), si agregas un curso a un instructor en la memoria de Java, también debes decirle al curso quién es su instructor. Para no olvidar esto, creamos métodos de conveniencia en la entidad Padre:
```java
// Dentro de Instructor.java
public void addCourse(Course tempCourse) {
    if (courses == null) {
        courses = new ArrayList<>();
    }
    courses.add(tempCourse); // Relación de ida
    tempCourse.setInstructor(this); // Relación de vuelta
}
```

### 3.3. Cheat Sheets Visuales de Mapeo
Resumen técnico basado en la documentación del curso:

| Mapping | Foreign Key location (Database) | Owning Side | Inverse side |
| :--- | :--- | :--- | :--- |
| **@OneToOne - Unidirectional**<br>Instructor -> InstructorDetail | `instructor` table | **Instructor**<br>`@OneToOne + @JoinColumn` | - |
| **@OneToOne - Bidirectional**<br>Instructor <-> InstructorDetail | `instructor` table | **Instructor**<br>`@OneToOne + @JoinColumn` | **InstructorDetail**<br>`@OneToOne(mappedBy="instructorDetail")` |
| **@OneToMany - Unidirectional**<br>Course -> Review | `review` table | **Course**<br>`@OneToMany + @JoinColumn` | - |
| **@OneToMany, @ManyToOne - Bidirectional**<br>Instructor <-> Course | `course` table | **Course**<br>`@ManyToOne + @JoinColumn` | **Instructor**<br>`@OneToMany(mappedBy="instructor")` |
| **@ManyToMany - Bidirectional**<br>Course <-> Student | Join table:<br>`course_student` | **Course**<br>`@ManyToMany + @JoinTable` | **Student**<br>`@ManyToMany(mappedBy="courses")` |

---

## 4. DAO vs Spring Data JPA

El curso enseña a usar el **Patrón DAO y el EntityManager** para entender la magia interna de Hibernate. En la vida real, utilizamos **Spring Data JPA (`JpaRepository`)**. Aquí están las equivalencias:

### Guardar una Entidad
* **Vía DAO:** `entityManager.persist(obj);` (o `merge`)
* **Vía JpaRepository:** `repository.save(obj);`

### Buscar por ID
* **Vía DAO:** `entityManager.find(Instructor.class, id);`
* **Vía JpaRepository:** `repository.findById(id);`

### Solucionar el Lazy Loading (Una Sola Consulta SQL Avanzada)
Cuando necesitas implementar un `JOIN FETCH` para traer un objeto LAZY en un solo viaje a la BD.

**Vía DAO (JPQL manual):**
```java
TypedQuery<Instructor> query = entityManager.createQuery(
    "SELECT i FROM Instructor i JOIN FETCH i.courses WHERE i.id = :data", Instructor.class);
query.setParameter("data", id);
return query.getSingleResult();
```

**Vía JpaRepository (La forma moderna con `@EntityGraph`):**
```java
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    // Le dice a Spring que haga un JOIN FETCH internamente del atributo "courses"
    @EntityGraph(attributePaths = {"courses"})
    Optional<Instructor> findById(Long id);
}
```

---

## 5. Cheat Sheet Técnico: Anotaciones Clave

* `@JoinColumn(name = "fk_columna")`: Le dice a Hibernate: *"Aquí es donde está la llave foránea física en mi tabla SQL"*.
* `mappedBy = "nombreDelAtributo"`: Le dice a Hibernate: *"Ve a buscar la configuración de la relación al objeto que está del otro lado, yo no soy el dueño de la llave foránea"*.
* `@JoinTable`: Se usa en `@ManyToMany` para definir la tabla intermedia. Requiere `joinColumns` (mi lado) e `inverseJoinColumns` (el otro lado).
* `@Transactional`: Mantiene la conexión (sesión) a la base de datos abierta hasta que el método termina. Permite, entre otras cosas, leer listas LAZY de forma segura.

---

## 6. FAQ y Notas del Desarrollador (Developer Notes)

Esta sección consolida los "bloqueos" y "momentos de iluminación" reales ocurridos durante el desarrollo de la sección.

### ❓ 6.1. ¿Por qué usamos `USE master` en los scripts de MS SQL Server?
Porque MS SQL Server es estricto con los contextos de conexión. Para hacer un `DROP DATABASE`, no puedes estar "parado" dentro de la base de datos que vas a destruir. `USE master` te lleva a la base de datos central del sistema, dándote perspectiva de administrador para eliminar y recrear la BD del proyecto, para luego volver a entrar con `USE [MI_NUEVA_BD]`.

### ❓ 6.2. La Sopa de Letras: JDBC vs JPA vs Hibernate vs Spring Data
1. **JDBC:** El código base nativo de Java para hablar con bases de datos.
2. **JPA:** Un manual de reglas. Es la API que define cómo *debería* funcionar el mapeo de objetos (ORM).
3. **Hibernate:** El motor real. El software que lee las reglas de JPA y ejecuta el código JDBC por debajo.
4. **Spring Data JPA:** El "Piloto Automático". Genera el código repetitivo del DAO (`JpaRepository`) dinámicamente, ahorrándonos escribir métodos manuales.

### ❓ 6.3. El Jefe Final: `LazyInitializationException` y el traidor `toString()`
Este es el error más común de Hibernate. Ocurre cuando intentas acceder a una lista LAZY pero la conexión a la base de datos ya se cerró.
* **El error:** Autogenerar un método `toString()` que incluye colecciones (`List<Course>`). Al imprimir el objeto, el `toString()` intenta acceder a los cursos (Proxy) cuando la sesión ya está muerta.
* **Solución:** NUNCA incluir listas con anotaciones `@OneToMany` o `@ManyToMany` dentro del método `toString()` de una entidad.

### ❓ 6.4. ¿Por qué se puede usar `getCourses()` sin error de LAZY Loading en ciertos métodos?
La clave es la anotación `@Transactional`. El comportamiento LAZY no significa que no puedas leer los datos, significa que se leerán *solo si se solicitan Y si la conexión sigue viva*. Cuando un método tiene `@Transactional`, Spring mantiene la sesión abierta. Si dentro invocas `getCourses()`, Hibernate se despierta, ejecuta un `SELECT` por debajo y te entrega los datos en tiempo real.