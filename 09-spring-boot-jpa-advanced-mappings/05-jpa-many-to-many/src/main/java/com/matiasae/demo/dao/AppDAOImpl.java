package com.matiasae.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.matiasae.demo.entity.Course;
import com.matiasae.demo.entity.Instructor;
import com.matiasae.demo.entity.InstructorDetail;
import com.matiasae.demo.entity.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class AppDAOImpl implements AppDAO {
    private EntityManager entityManager;

    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void save(Instructor instructor) {
        entityManager.persist(instructor);
    }

    @Override
    public Instructor findInstructorById(Long id) {
        return entityManager.find(Instructor.class, id);
    }

    // transactional allows to use getCourses() without getting LazyInitializationException
    // because the session is open until the transaction is completed
    @Transactional
    @Override
    public void deleteInstructorById(Long id) {
        Instructor tempInstructor = entityManager.find(Instructor.class, id);
        if (tempInstructor != null) {
            // List<Course> courses = findCoursesByInstructorId(id);
            List<Course> courses = tempInstructor.getCourses();
            for (Course course : courses) {
                course.setInstructor(null);
            }

            entityManager.remove(tempInstructor);
        }
    }

    @Override
    public InstructorDetail findInstructorDetailById(Long id) {
        return entityManager.find(InstructorDetail.class, id);
    }

    @Transactional
    @Override
    public InstructorDetail deleteInstructorDetailById(Long id) {
        InstructorDetail tempInstructorDetail = entityManager.find(InstructorDetail.class, id);

        if (tempInstructorDetail != null) {
            // CASCADE NOT DELETE: remove the associated object reference
            // break bi-directional link
            tempInstructorDetail.getInstructor().setInstructorDetail(null);
            // -------------------------------
            entityManager.remove(tempInstructorDetail);
        }

        return tempInstructorDetail;
    }

    @Override
    public List<Course> findCoursesByInstructorId(Long instructorId) {
        TypedQuery<Course> query = entityManager.createQuery(
            "SELECT c FROM Course c WHERE c.instructor.id = :data", Course.class);
        query.setParameter("data", instructorId);
        return query.getResultList();
    }

    @Override
    public Instructor findInstructorByIdJoinFetch(Long instructorId) {
        // JOIN FETCH courses + JOIN FETCH instructorDetail = minimize the number of queries to 1 (performance optimization)
        TypedQuery<Instructor> query = entityManager.createQuery(
            "SELECT i FROM Instructor i JOIN FETCH i.courses JOIN FETCH i.instructorDetail WHERE i.id = :data", Instructor.class);
        query.setParameter("data", instructorId);
        return query.getSingleResult();
    }

    @Transactional
    @Override
    public void update(Instructor instructor) {
        entityManager.merge(instructor);
    }

    @Override
    public Course findCourseById(Long id) {
        return entityManager.find(Course.class, id);
    }

    @Transactional
    @Override
    public void update(Course course) {
        entityManager.merge(course);
    }

    @Transactional
    @Override
    public void deleteCourseById(Long id) {
        Course tempCourse = entityManager.find(Course.class, id);
        if (tempCourse != null) {
            entityManager.remove(tempCourse);
        }
    }

    @Transactional
    @Override
    public void save(Course course) {
        entityManager.persist(course);
    }

    @Override
    public Course findCourseByIdWithReviews(Long id) {
        TypedQuery<Course> query = entityManager.createQuery(
            "SELECT c FROM Course c JOIN FETCH c.reviews WHERE c.id = :data", Course.class);
        query.setParameter("data", id);
        return query.getSingleResult();
    }

    @Override
    public Course findCourseAndStudentsByCourseId(Long courseId) {
        TypedQuery<Course> query = entityManager.createQuery(
            "SELECT c FROM Course c JOIN FETCH c.students WHERE c.id = :data", Course.class);
        query.setParameter("data", courseId);
        return query.getSingleResult();
    }

    @Override
    public Student findStudentAndCoursesByStudentId(Long studentId) {
        TypedQuery<Student> query = entityManager.createQuery(
            "SELECT s FROM Student s JOIN FETCH s.courses WHERE s.id = :data", Student.class);
        query.setParameter("data", studentId);
        return query.getSingleResult();
    }

    @Transactional
    @Override
    public void update(Student student) {
        entityManager.merge(student);
    }

    @Transactional
    @Override
    public void deleteStudentById(Long id) {
        Student tempStudent = entityManager.find(Student.class, id);
        if (tempStudent != null) {
           List<Course> courses = tempStudent.getCourses();
           for (Course course : courses) {
                course.getStudents().remove(tempStudent);
           }
            entityManager.remove(tempStudent);
        }
    }
}
