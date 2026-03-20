USE [employee_directory];
GO

-- Eliminamos las tablas si ya existen (El orden importa por la Foreign Key)
DROP TABLE IF EXISTS [authorities];
DROP TABLE IF EXISTS [users];
GO

--
-- Estructura de la tabla [users]
--
CREATE TABLE [users] (
  [username] varchar(50) NOT NULL,
  [password] char(68) NOT NULL,
  [enabled] tinyint NOT NULL,
  PRIMARY KEY ([username])
);
GO

--
-- Insertando datos en la tabla [users]
--
-- NOTA: Las contraseñas están encriptadas usando BCrypt
-- Una herramienta de generación está disponible en: https://www.luv2code.com/generate-bcrypt-password
--
-- La contraseña por defecto aquí es: fun123
--

INSERT INTO [users] ([username], [password], [enabled])
VALUES 
('john', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1),
('mary', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1),
('susan', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1);
GO

--
-- Estructura de la tabla [authorities]
--
CREATE TABLE [authorities] (
  [username] varchar(50) NOT NULL,
  [authority] varchar(50) NOT NULL,
  CONSTRAINT [UQ_authorities4_idx_1] UNIQUE ([username], [authority]),
  CONSTRAINT [FK_authorities4_ibfk_1] FOREIGN KEY ([username]) REFERENCES [users] ([username])
);
GO

--
-- Insertando datos en la tabla [authorities]
--

INSERT INTO [authorities] ([username], [authority])
VALUES 
('john', 'ROLE_EMPLOYEE'),
('mary', 'ROLE_EMPLOYEE'),
('mary', 'ROLE_MANAGER'),
('susan', 'ROLE_EMPLOYEE'),
('susan', 'ROLE_MANAGER'),
('susan', 'ROLE_ADMIN');
GO