# Sección 10: Programación Orientada a Aspectos (AOP) en Spring Boot

Este documento es una guía de referencia completa sobre AOP en Spring Boot. AOP no reemplaza a la Programación Orientada a Objetos (OOP), sino que la complementa resolviendo un problema arquitectónico específico: **El Código Espagueti causado por los Intereses Transversales (Cross-Cutting Concerns).**



## 1. ¿Qué es AOP y por qué lo usamos?
Imagina que tienes requerimientos como *Logging, Seguridad, Manejo de Transacciones o Caché*. En OOP tradicional, tendrías que copiar y pegar este código al principio y al final de cada método de tu aplicación. 

**AOP resuelve esto interceptando la ejecución del código.** Permite encapsular esta lógica repetitiva en un solo lugar (un Aspecto) y decirle a Spring que la inyecte dinámicamente "al vuelo" en los métodos que cumplan con ciertas reglas, manteniendo nuestra lógica de negocio 100% limpia.

---

## 2. El Diccionario de AOP (Terminología Clave)

Entender AOP requiere conocer su vocabulario especial:

* **Aspect:** Es la clase Java (marcada con `@Aspect`) que agrupa todo nuestro código transversal (ej. `MyLoggingAspect`). Es el "Guardia de Seguridad".
* **Advice:** Es el QUÉ y el CUÁNDO. Es el método dentro de nuestro Aspecto que contiene la acción a ejecutar (ej. hacer un log) y el momento en que se ejecuta (ej. *Antes* del método).
* **Pointcut:** Es el DÓNDE. Es la expresión (parecida a una expresión regular) que define a qué métodos de nuestra aplicación se les aplicará el Advice. (Ej: `"execution(* com.miapp.dao.*.*(..))"`).
* **JoinPoint:** Es el momento exacto en tiempo de ejecución donde se interceptó un método. Nos permite acceder a los metadatos de ese método específico (su nombre, sus parámetros, etc.).

---

## 3. Tipos de Advice, Casos de Uso y Ejemplos

Existen 5 tipos de Advice en Spring AOP, que se diferencian puramente en **cuándo** se ejecutan en relación al método interceptado (Target Method).

### 3.1. `@Before` (Antes)
* **Cuándo se ejecuta:** Justo antes de que el método destino comience.
* **Casos de uso comunes:** Logs de auditoría, verificaciones de seguridad (roles/permisos), validación inicial de datos.
* **Ejemplo:**
    ```java
    @Before("execution(* com.demo.service.*.save*(..))")
    public void logBeforeSave(JoinPoint joinPoint) {
        System.out.println(">>> Verificando permisos antes de guardar: " + joinPoint.getSignature().getName());
    }
    ```

### 3.2. `@AfterReturning` (Después de Retornar con Éxito)
* **Cuándo se ejecuta:** Después de que el método destino termina exitosamente (sin lanzar excepciones).
* **Casos de uso comunes:** Loguear el resultado exitoso de una operación, auditar datos, e incluso **modificar el valor retornado** antes de que llegue a quien llamó al método (ej. enmascarar contraseñas o formatear textos).
* **Ejemplo:**
    ```java
    @AfterReturning(pointcut = "execution(* findAccounts(..))", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, List<Account> result) {
        System.out.println(">>> El método retornó: " + result.size() + " cuentas.");
        // Podríamos modificar la lista 'result' aquí.
    }
    ```

### 3.3. `@AfterThrowing` (Después de una Excepción)
* **Cuándo se ejecuta:** Solo si el método destino "explota" y lanza una excepción.
* **Casos de uso comunes:** Centralizar el log de errores, enviar alertas por correo/Slack a los administradores, auditoría de fallos. (Ojo: Este Advice no detiene la excepción, la excepción seguirá su camino hacia el usuario).
* **Ejemplo:**
    ```java
    @AfterThrowing(pointcut = "execution(* processPayment(..))", throwing = "theExc")
    public void logAfterException(JoinPoint joinPoint, Throwable theExc) {
        System.out.println("!!! ALERTA FATAL en: " + joinPoint.getSignature().toShortString());
        System.out.println("!!! Causa: " + theExc.getMessage());
    }
    ```

### 3.4. `@After` (Finalmente / Indistintamente)
* **Cuándo se ejecuta:** Se ejecuta SIEMPRE después del método, sin importar si tuvo éxito o lanzó una excepción (funciona igual que un bloque `finally` en un `try-catch`).
* **Casos de uso comunes:** Liberar recursos, cerrar conexiones a bases de datos o archivos temporales, limpieza (cleanup). No puede ver la excepción ni el resultado de retorno.
* **Ejemplo:**
    ```java
    @After("execution(* com.demo.dao.*.*(..))")
    public void cleanUpResources(JoinPoint joinPoint) {
        System.out.println(">>> Limpieza finalizando ejecución de " + joinPoint.getSignature().getName());
    }
    ```

### 3.5. `@Around` (Antes y Después)
* **Cuándo se ejecuta:** Intercepta la llamada *antes* de que llegue al método, y toma el control *después* de que termina. Literalmente "envuelve" al método.
* **Casos de uso comunes:** Profiling (cronometrar cuánto tarda un método en milisegundos), gestión estricta de transacciones, sistemas de Caché (si el dato está en caché, retorna el dato sin siquiera ejecutar el método original), y tragar/manejar excepciones para que no lleguen al usuario.
* **Ejemplo:** *(Ver sección 4 para entender el ProceedingJoinPoint)*.

---

## 4. El Gran Concepto: `JoinPoint` vs `ProceedingJoinPoint`

Esta es una de las preguntas de arquitectura más importantes en AOP. Ambos se inyectan como parámetros en tus métodos de Advice, pero tienen propósitos muy distintos.

### `JoinPoint`
* **Dónde se usa:** En `@Before`, `@AfterReturning`, `@AfterThrowing`, `@After`.
* **Qué hace:** Es un objeto de "solo lectura". Te permite inspeccionar el método que fue interceptado.
* **Métodos útiles:** * `joinPoint.getSignature()`: Devuelve información del método (nombre, tipo de retorno).
    * `joinPoint.getArgs()`: Devuelve un array con los argumentos que el usuario le pasó al método.

### `ProceedingJoinPoint`
* **Dónde se usa:** **EXCLUSIVAMENTE en `@Around`.**
* **Qué hace:** Hereda todo lo que tiene `JoinPoint`, pero agrega **el control de la ejecución**. Como `@Around` intercepta el método antes de que nazca, si no le dices explícitamente al método que avance, el método destino jamás se ejecutará.
* **Método clave:** `proceed()`. Esto dispara la ejecución del método original.
* **Ejemplo práctico de `@Around` con `ProceedingJoinPoint` (Cronómetro):**
    ```java
    @Around("execution(* com.demo.service.*.*(..))")
    public Object profileMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 1. ANTES: Empezamos el cronómetro
        long start = System.currentTimeMillis();
        
        // 2. EJECUCIÓN: Le decimos al método original que arranque
        Object result = proceedingJoinPoint.proceed(); 
        
        // 3. DESPUÉS: Calculamos el tiempo
        long end = System.currentTimeMillis();
        System.out.println(">>> El método tardó: " + (end - start) + " ms");
        
        // 4. RETORNO: Devolvemos el resultado original al usuario
        return result; 
    }
    ```

---

## 5. Declaraciones Pointcut (`@Pointcut`)

Para evitar copiar y pegar la misma expresión larga `"execution(public * com.miapp...)"` en todos los Advices, Spring permite centralizarlas creando métodos vacíos anotados con `@Pointcut`.

```java
@Aspect
@Component
public class AopExpressions {

    // Declaramos el Pointcut una sola vez
    @Pointcut("execution(* com.demo.dao.*.*(..))")
    public void forDaoPackage() {}

    // Lo reciclamos usando el nombre del método
    @Before("forDaoPackage()")
    public void beforeDao() { ... }
}
```
*Tip de Arquitectura:* Es una buena práctica poner todos los `@Pointcut` en una clase separada (ej. `AopDeclarations`) e importarlos en los distintos aspectos usando su ruta completa del paquete.

---
## 💡 Notas del Desarrollador (Developer Notes)
* AOP por defecto solo funciona sobre **Spring Beans**. Si instancias una clase tú mismo usando `new MiObjeto()`, Spring AOP no podrá interceptarla.
* La anotación `@Transactional` de Spring Data JPA que usamos en la Sección 9 es un Aspecto AOP bajo el capó (usa un `@Around` que abre la transacción antes del método y le hace commit o rollback después).