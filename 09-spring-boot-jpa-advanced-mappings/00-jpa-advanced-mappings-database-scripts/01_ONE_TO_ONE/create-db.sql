-- 1. Creamos la base de datos desde cero (Equivalente al DROP/CREATE SCHEMA de MySQL)
USE master;
GO

IF DB_ID('DB_ONE_TO_ONE') IS NOT NULL
BEGIN
    -- Expulsa conexiones activas para poder borrarla
    ALTER DATABASE DB_ONE_TO_ONE SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE DB_ONE_TO_ONE;
END
GO

CREATE DATABASE DB_ONE_TO_ONE;
GO

USE DB_ONE_TO_ONE;
GO

-- 2. Creamos la tabla Padre (instructor_detail)
CREATE TABLE instructor_detail (
    id int IDENTITY(1,1) NOT NULL,
    youtube_channel varchar(128) DEFAULT NULL,
    hobby varchar(45) DEFAULT NULL,
    CONSTRAINT PK_instructor_detail PRIMARY KEY (id)
);
GO

-- 3. Creamos la tabla Hija (instructor) con su Foreign Key
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