-- 1. Nos ubicamos en master por seguridad
USE master;
GO

-- 2. Creamos la base de datos si no existe
IF DB_ID('EMPLOYEE_DIRECTORY') IS NULL
BEGIN
    CREATE DATABASE [EMPLOYEE_DIRECTORY];
END
GO

-- 3. Usamos la base de datos
USE [EMPLOYEE_DIRECTORY];
GO

-- 4. Borramos la tabla si ya existía de pruebas anteriores
IF OBJECT_ID('employee', 'U') IS NOT NULL
BEGIN
    DROP TABLE employee;
END
GO

-- 5. Creamos la tabla con IDENTITY para el autoincremento
CREATE TABLE employee (
    id int IDENTITY(1,1) NOT NULL,
    first_name varchar(45) DEFAULT NULL,
    last_name varchar(45) DEFAULT NULL,
    email varchar(45) DEFAULT NULL,
    CONSTRAINT PK_employee PRIMARY KEY (id)
);
GO

-- 6. Apagamos el bloqueo de seguridad temporalmente para insertar IDs manuales
SET IDENTITY_INSERT employee ON;
GO

-- 7. Insertamos la data (es obligatorio especificar las columnas al usar IDENTITY_INSERT)
INSERT INTO employee (id, first_name, last_name, email) VALUES 
  (1, 'Leslie', 'Andrews', 'leslie@luv2code.com'),
  (2, 'Emma', 'Baumgarten', 'emma@luv2code.com'),
  (3, 'Avani', 'Gupta', 'avani@luv2code.com'),
  (4, 'Yuri', 'Petrov', 'yuri@luv2code.com'),
  (5, 'Juan', 'Vega', 'juan@luv2code.com');
GO

-- 8. Volvemos a encender el bloqueo para que los próximos se generen solos
SET IDENTITY_INSERT employee OFF;
GO