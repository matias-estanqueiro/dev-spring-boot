# Spring Core: Resumen de la Sección 2

Esta sección introduce el corazón de la arquitectura de Spring Framework, centrándose en cómo el framework toma el control del ciclo de vida de los objetos y sus dependencias para crear aplicaciones desacopladas y escalables.

## 1. El Concepto Fundamental: ¿Qué es un Bean?

En el contexto de Spring, un **Bean** no es más que un objeto Java estándar. La única diferencia radica en que **no es instanciado ni gestionado por el programador (no usas `new`)**, sino que es instanciado, ensamblado y administrado completamente por el contenedor de Spring (Spring IoC Container).
Cualquier clase decorada con `@Component`, `@RestController`, `@Service` o `@Repository` se convierte automáticamente en un Bean al iniciar la aplicación.

## 2. Inversión de Control (IoC - Inversion of Control)

La Inversión de Control es un principio de diseño arquitectónico. En la programación tradicional, tu código personalizado llama a las librerías para hacer tareas (tú tienes el control). En IoC, **el framework llama a tu código personalizado** (se invierte el control).



**Explicación detallada:**
En lugar de que tu clase `Controlador` sea responsable de crear explícitamente las dependencias que necesita (ej. `Coach myCoach = new CricketCoach();`), delegas (inviertes) esa responsabilidad al "Application Context" de Spring. Spring actúa como una fábrica omnisciente: escanea el código, crea las instancias necesarias al arrancar y las mantiene en memoria, decidiendo cuándo y cómo entregarlas.

## 3. Inyección de Dependencias (DI - Dependency Injection)

La Inyección de Dependencias es el patrón de diseño (la técnica específica) que Spring utiliza para implementar la Inversión de Control. Consiste en proporcionar (inyectar) a un objeto las dependencias que necesita para funcionar, desde el exterior.



Spring Boot ofrece tres formas de inyección, aunque el curso se centra en las dos principales:
* **Constructor Injection (Recomendada):** La dependencia se solicita a través del constructor de la clase. Es el estándar de la industria porque garantiza que el objeto no pueda ser instanciado sin sus dependencias obligatorias (falla rápido si falta algo).
* **Setter Injection:** La dependencia se inyecta a través de un método `set()`. Se utiliza raramente, solo para dependencias opcionales que pueden cambiar en tiempo de ejecución.
* *(Nota: Field Injection con `@Autowired` sobre la variable directa está obsoleta y se considera una mala práctica arquitectónica).*

## 4. Resolución de Ambigüedades: `@Primary` vs `@Qualifier`

Cuando múltiples Beans implementan la misma interfaz (ej. `CricketCoach` y `BaseballCoach` implementan `Coach`), Spring arroja una excepción `NoUniqueBeanDefinitionException` porque no sabe cuál inyectar. Tienes dos herramientas para solucionarlo:

* **`@Primary`:** Se coloca en la clase del Bean (ej. en `CricketCoach`). Le dice a Spring: *"Si alguien pide un Coach y no especifica cuál, dame a mí por defecto"*.
* **`@Qualifier("nombreDelBean")`:** Se coloca en el punto de inyección (ej. en el constructor del controlador). Le dice a Spring: *"Quiero exactamente el Bean con este ID ignorando cualquier opción por defecto"*.

**Orden de peso (Precedencia):**
**`@Qualifier` tiene mayor jerarquía que `@Primary`.** Si tienes un `BaseballCoach` marcado como `@Primary`, pero en tu controlador usas `@Qualifier("cricketCoach")`, Spring ignorará el Primary e inyectará el Cricket. El Qualifier es específico y tiene la última palabra.

## 5. Scopes de los Beans (Alcance)

El "Scope" define el ciclo de vida y la visibilidad de un Bean. 
* **Singleton (Por defecto):** Spring crea una **única instancia** del Bean y la comparte con todos los que la soliciten. Todos apuntan al mismo espacio en memoria.
* **Prototype:** Spring crea una **nueva instancia** desde cero cada vez que se solicita el Bean.

**`@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)` vs `@Scope("prototype")`**
Técnicamente, **hacen exactamente lo mismo** y el resultado en compilación es idéntico. Sin embargo, en el mundo profesional **siempre se utiliza la constante** (`ConfigurableBeanFactory.SCOPE_PROTOTYPE`).
* `"prototype"` es un "magic string" (cadena de texto mágica). Si escribes mal `@Scope("prototipe")`, el compilador de Java no detectará el error, y tu programa fallará en tiempo de ejecución.
* `ConfigurableBeanFactory.SCOPE_PROTOTYPE` es una constante segura (Type-safe). Si te equivocas al escribirla, el IDE te marcará un error de sintaxis inmediatamente antes de compilar.

## 6. Ciclo de Vida del Bean y el problema con `@PreDestroy`

Puedes interceptar el ciclo de vida de un Bean usando `@PostConstruct` (ejecuta código justo después de que Spring inyecta las dependencias) y `@PreDestroy` (ejecuta código justo antes de que el Bean sea destruido y la memoria liberada).

**¿Por qué no ves el mensaje de `@PreDestroy` en la consola de VSCode?**
Existen dos razones técnicas para esto:

1.  **El método de detención (SIGKILL vs SIGTERM):** Cuando presionas el botón rojo cuadrado ("Stop") en VSCode, el IDE a menudo envía una señal de "Force Kill" (`SIGKILL`) al sistema operativo. Esto aniquila el proceso de la Máquina Virtual de Java instantáneamente. Spring no tiene tiempo de ejecutar sus rutinas de apagado seguro (graceful shutdown) y el método `@PreDestroy` jamás es llamado.
    * *Solución:* Para ver el mensaje, ejecuta tu aplicación desde la terminal integrada de VSCode (`./mvnw spring-boot:run`) y detenla presionando `Ctrl + C`. Esto envía una señal `SIGTERM` que permite a Spring apagarse ordenadamente e imprimir el mensaje.
2.  **El Scope del Bean:** Es una regla estricta de Spring: **Spring NO llama a `@PreDestroy` en beans con scope Prototype**. El contenedor instancia los prototypes y se los entrega al cliente, pero no lleva un registro de ellos para destruirlos. El recolector de basura (Garbage Collector) de Java se encarga de eliminarlos después. `@PreDestroy` solo funciona garantizadamente en beans Singleton.