USE [employee_directory];
GO

-- Eliminamos las tablas si ya existen (El orden importa por la Foreign Key)
DROP TABLE IF EXISTS [roles];
DROP TABLE IF EXISTS [members];
GO

--
-- Estructura de la tabla [members]
--
CREATE TABLE [members] (
  [user_id] varchar(50) NOT NULL,
  [pw] char(68) NOT NULL,
  [active] tinyint NOT NULL,
  PRIMARY KEY ([user_id])
);
GO

--
-- Insertando datos en la tabla [members]
--
-- NOTA: Las contraseñas están encriptadas usando BCrypt
-- Una herramienta de generación está disponible en: https://www.luv2code.com/generate-bcrypt-password
--
-- La contraseña por defecto aquí es: fun123
--

INSERT INTO [members] ([user_id], [pw], [active])
VALUES
('john', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1),
('mary', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1),
('susan', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1);
GO

--
-- Estructura de la tabla [roles]
--
CREATE TABLE [roles] (
  [user_id] varchar(50) NOT NULL,
  [role] varchar(50) NOT NULL,
  CONSTRAINT [UQ_authorities5_idx_1] UNIQUE ([user_id], [role]),
  CONSTRAINT [FK_authorities5_ibfk_1] FOREIGN KEY ([user_id]) REFERENCES [members] ([user_id])
);
GO

--
-- Insertando datos en la tabla [roles]
--

INSERT INTO [roles] ([user_id], [role])
VALUES
('john', 'ROLE_EMPLOYEE'),
('mary', 'ROLE_EMPLOYEE'),
('mary', 'ROLE_MANAGER'),
('susan', 'ROLE_EMPLOYEE'),
('susan', 'ROLE_MANAGER'),
('susan', 'ROLE_ADMIN');
GO