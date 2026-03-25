package com.matiasae.demo.dao;

import java.util.List;

import com.matiasae.demo.entity.Course;
import com.matiasae.demo.entity.Instructor;
import com.matiasae.demo.entity.InstructorDetail;
import com.matiasae.demo.entity.Student;

public interface AppDAO {
    void save(Instructor instructor);
    Instructor findInstructorById(Long id);
    void deleteInstructorById(Long id);

    InstructorDetail findInstructorDetailById(Long id);
    InstructorDetail deleteInstructorDetailById(Long id);

    // Lazy Loading
    List<Course> findCoursesByInstructorId(Long instructorId);

    // JOIN FETCH
    Instructor findInstructorByIdJoinFetch(Long instructorId);
    
    Course findCourseById(Long id);

    void update(Instructor instructor);
    void update(Course course);

    void deleteCourseById(Long id);

    // One to Many Unidirectional
    void save(Course course);
    Course findCourseByIdWithReviews(Long id);

    // Many to Many
    Course findCourseAndStudentsByCourseId(Long courseId);
    Student findStudentAndCoursesByStudentId(Long studentId);
    void update(Student student);
    void deleteStudentById(Long id);

}
