USE [EMPLOYEE_DIRECTORY];
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
  [password] varchar(50) NOT NULL,
  [enabled] tinyint NOT NULL,
  PRIMARY KEY ([username])
);
GO

--
-- Insertando datos en la tabla [users]
--
INSERT INTO [users] ([username], [password], [enabled])
VALUES 
('john', '{noop}test123', 1),
('mary', '{noop}test123', 1),
('susan', '{noop}test123', 1);
GO

--
-- Estructura de la tabla [authorities]
--
CREATE TABLE [authorities] (
  [username] varchar(50) NOT NULL,
  [authority] varchar(50) NOT NULL,
  CONSTRAINT [UQ_authorities_idx_1] UNIQUE ([username], [authority]),
  CONSTRAINT [FK_authorities_ibfk_1] FOREIGN KEY ([username]) REFERENCES [users] ([username])
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