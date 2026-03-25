-- 1. Nos ubicamos en master para poder operar la base de datos
USE master;
GO

-- 2. Limpieza segura: si existe, la borramos
IF DB_ID('DB_ONE_TO_MANY_UNI') IS NOT NULL
BEGIN
    ALTER DATABASE [DB_ONE_TO_MANY_UNI] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [DB_ONE_TO_MANY_UNI];
END
GO

-- 3. Creamos la base de datos con el nombre solicitado
CREATE DATABASE [DB_ONE_TO_MANY_UNI];
GO

-- 4. Entramos a la base de datos
USE [DB_ONE_TO_MANY_UNI];
GO

-- ==========================================
-- CREACIÓN DE TABLAS (De Padre a Hijos)
-- ==========================================

-- Tabla 1: instructor_detail
CREATE TABLE instructor_detail (
    id int IDENTITY(1,1) NOT NULL,
    youtube_channel varchar(128) DEFAULT NULL,
    hobby varchar(45) DEFAULT NULL,
    CONSTRAINT PK_instructor_detail PRIMARY KEY (id)
);
GO

-- Tabla 2: instructor
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

-- Tabla 3: course (Arranca en 10)
CREATE TABLE course (
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

-- Tabla 4: review (LA NUEVA TABLA)
CREATE TABLE review (
    id int IDENTITY(1,1) NOT NULL,
    comment varchar(256) DEFAULT NULL,
    course_id int DEFAULT NULL,
    CONSTRAINT PK_review PRIMARY KEY (id),
    CONSTRAINT FK_COURSE FOREIGN KEY (course_id) 
        REFERENCES course (id) 
        ON DELETE NO ACTION 
        ON UPDATE NO ACTION
);
GO