-- Creamos la base de datos si no existe
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'EMPLOYEE_DIRECTORY')
BEGIN
    CREATE DATABASE [EMPLOYEE_DIRECTORY];
END
GO

USE [EMPLOYEE_DIRECTORY];
GO

-- Eliminamos la tabla si ya existe
DROP TABLE IF EXISTS [employee];
GO

--
-- Estructura de la tabla [employee]
--
CREATE TABLE [employee] (
  [id] int NOT NULL IDENTITY(1,1),
  [first_name] varchar(45) DEFAULT NULL,
  [last_name] varchar(45) DEFAULT NULL,
  [email] varchar(45) DEFAULT NULL,
  PRIMARY KEY ([id])
);
GO

--
-- Insertando datos en la tabla [employee]
--

-- Habilitamos la inserción explícita de IDs
SET IDENTITY_INSERT [employee] ON;
GO

INSERT INTO [employee] ([id], [first_name], [last_name], [email]) 
VALUES 
  (1, 'Leslie', 'Andrews', 'leslie@luv2code.com'),
  (2, 'Emma', 'Baumgarten', 'emma@luv2code.com'),
  (3, 'Avani', 'Gupta', 'avani@luv2code.com'),
  (4, 'Yuri', 'Petrov', 'yuri@luv2code.com'),
  (5, 'Juan', 'Vega', 'juan@luv2code.com');
GO

-- Deshabilitamos la inserción explícita para que el motor siga generando los próximos IDs automáticamente
SET IDENTITY_INSERT [employee] OFF;
GO