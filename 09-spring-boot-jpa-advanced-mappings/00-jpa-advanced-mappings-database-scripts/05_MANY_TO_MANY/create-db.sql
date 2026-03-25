-- 1. Nos ubicamos en master
USE master;
GO

-- 2. Limpieza segura de la base de datos
IF DB_ID('DB_MANY_TO_MANY') IS NOT NULL
BEGIN
    ALTER DATABASE [DB_MANY_TO_MANY] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [DB_MANY_TO_MANY];
END
GO

-- 3. Creamos la nueva base de datos
CREATE DATABASE [DB_MANY_TO_MANY];
GO

-- 4. Entramos a la base de datos
USE [DB_MANY_TO_MANY];
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

-- Tabla 4: review (One-To-Many Unidireccional desde Course)
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

-- Tabla 5: student (NUEVA TABLA)
CREATE TABLE student (
    id int IDENTITY(1,1) NOT NULL,
    first_name varchar(45) DEFAULT NULL,
    last_name varchar(45) DEFAULT NULL,
    email varchar(45) DEFAULT NULL,
    CONSTRAINT PK_student PRIMARY KEY (id)
);
GO

-- Tabla 6: course_student (NUEVA TABLA INTERMEDIA / JOIN TABLE)
CREATE TABLE course_student (
    course_id int NOT NULL,
    student_id int NOT NULL,
    -- La llave primaria es compuesta: la combinación de curso y estudiante
    CONSTRAINT PK_course_student PRIMARY KEY (course_id, student_id),
    -- Llave foránea hacia course
    CONSTRAINT FK_COURSE_05 FOREIGN KEY (course_id) 
        REFERENCES course (id) 
        ON DELETE NO ACTION 
        ON UPDATE NO ACTION,
    -- Llave foránea hacia student
    CONSTRAINT FK_STUDENT FOREIGN KEY (student_id) 
        REFERENCES student (id) 
        ON DELETE NO ACTION 
        ON UPDATE NO ACTION
);
GO