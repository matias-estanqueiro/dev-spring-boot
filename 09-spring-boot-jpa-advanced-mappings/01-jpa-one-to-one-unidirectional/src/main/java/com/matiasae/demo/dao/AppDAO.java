package com.matiasae.demo.dao;

import com.matiasae.demo.entity.Instructor;

public interface AppDAO {
    void save(Instructor instructor);
    Instructor findInstructorById(Long id);
    void deleteInstructorById(Long id);
}
