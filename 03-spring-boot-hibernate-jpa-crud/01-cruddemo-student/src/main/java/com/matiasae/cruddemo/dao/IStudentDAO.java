package com.matiasae.cruddemo.dao;

import java.util.List;

import com.matiasae.cruddemo.entity.Student;

public interface IStudentDAO {
    void save(Student student);
    Student findById(Integer id);
    List<Student> findAll();
    List<Student> findByLastName(String lastName);
    Student update(Student student);
    void deleteById(Integer id);
    Integer deleteAll();
}
