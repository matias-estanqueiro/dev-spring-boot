package com.matiasae.demo.dao;

import com.matiasae.demo.entity.Instructor;
import com.matiasae.demo.entity.InstructorDetail;

public interface AppDAO {
    void save(Instructor instructor);
    Instructor findInstructorById(Long id);
    void deleteInstructorById(Long id);

    InstructorDetail findInstructorDetailById(Long id);
    InstructorDetail deleteInstructorDetailById(Long id);
}
