-- 1. Nos ubicamos en el "cerebro" del servidor
USE master;
GO

-- 2. Si la base de datos ya existe, la expulsamos y la borramos
IF DB_ID('DB_ONE_TO_MANY') IS NOT NULL
BEGIN
    ALTER DATABASE [DB_ONE_TO_MANY] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [DB_ONE_TO_MANY];
END
GO

-- 3. Creamos la base de datos con el nombre exacto solicitado
CREATE DATABASE [DB_ONE_TO_MANY];
GO

-- 4. Ingresamos a la nueva base de datos
USE [DB_ONE_TO_MANY];
GO

-- ==========================================
-- CREACIÓN DE TABLAS (Ordenadas de Padre a Hijos)
-- ==========================================

-- Tabla 1: Detalles del Instructor (Padre independiente)
CREATE TABLE instructor_detail (
    id int IDENTITY(1,1) NOT NULL,
    youtube_channel varchar(128) DEFAULT NULL,
    hobby varchar(45) DEFAULT NULL,
    CONSTRAINT PK_instructor_detail PRIMARY KEY (id)
);
GO

-- Tabla 2: Instructor (Hijo de Detail, Padre de Course)
CREATE TABLE instructor (
    id int IDENTITY(1,1) NOT NULL,
    first_name varchar(45) DEFAULT NULL,
    last_name varchar(45) DEFAULT NULL,
    email varchar(45) DEFAULT NULL,
    instructor_detail_id int DEFAULT NULL,
    CONSTRAINT PK_instructor PRIMARY KEY (id),
    CONSTRAINT FK_DETAIL FOREIGN KEY (instructor_detail_id) 
        REFERENCES instructor_detail (id) 
        ON DELETE NO ACTION 
        ON UPDATE NO ACTION
);
GO

-- Tabla 3: Curso (Hijo de Instructor)
CREATE TABLE course (
    -- Fíjate que aquí arranca en 10, tal como lo pediste en el script original
    id int IDENTITY(10,1) NOT NULL,
    title varchar(128) DEFAULT NULL,
    instructor_id int DEFAULT NULL,
    CONSTRAINT PK_course PRIMARY KEY (id),
    CONSTRAINT TITLE_UNIQUE UNIQUE (title),
    CONSTRAINT FK_INSTRUCTOR FOREIGN KEY (instructor_id) 
        REFERENCES instructor (id) 
        ON DELETE NO ACTION 
        ON UPDATE NO ACTION
);
GO