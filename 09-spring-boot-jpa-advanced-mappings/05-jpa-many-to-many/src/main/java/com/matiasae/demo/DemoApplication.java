package com.matiasae.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matiasae.demo.dao.AppDAO;
import com.matiasae.demo.entity.Course;
import com.matiasae.demo.entity.Student;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AppDAO appDAO) {
        return runner -> {
            // MANY-TO-MANY MAPPING
            // --------------------
            // createCourseAndStudents(appDAO);
            // findCourseAndStudents(appDAO);
            // findStudentAndCourses(appDAO);
            // addMoreCoursesToStudent(appDAO);
            // deleteCourse(appDAO);
            deleteStudent(appDAO);

        };
    }

    private void createCourseAndStudents(AppDAO appDAO) {
        // create a course
        Course tempCourse = new Course("Pacman - How To Score One Million Points");

        // create the students
        Student tempStudent1 = new Student("John", "Doe", "john.doe@example.com");
        Student tempStudent2 = new Student("Mary", "Public", "mary.public@example.com");

        // add students to the course
        tempCourse.addStudent(tempStudent1);
        tempCourse.addStudent(tempStudent2);

        // since we have CascadeType.PERSIST, we don't need to separately save the students
        appDAO.save(tempCourse);
    }

    private void findCourseAndStudents(AppDAO appDAO) {
        Long courseId = 10L;
        Course tempCourse = appDAO.findCourseAndStudentsByCourseId(courseId);
        System.out.println("Course: " + tempCourse);
        System.out.println("Students: " + tempCourse.getStudents());
    }

    private void findStudentAndCourses(AppDAO appDAO) {
        Long studentId = 2L;
        Student tempStudent = appDAO.findStudentAndCoursesByStudentId(studentId);
        System.out.println("Student: " + tempStudent);
        System.out.println("Courses: " + tempStudent.getCourses());
    }

    private void addMoreCoursesToStudent(AppDAO appDAO) {
        Long studentId = 2L;
        Student tempStudent = appDAO.findStudentAndCoursesByStudentId(studentId);

        Course tempCourse1 = new Course("Rubik's Cube - How To Speed Cube");
        Course tempCourse2 = new Course("Atari 2600 - Game Development");

        tempStudent.addCourse(tempCourse1);
        tempStudent.addCourse(tempCourse2);

        // since we have CascadeType.PERSIST, we don't need to separately save the courses
        appDAO.update(tempStudent);
    }

    private void deleteCourse(AppDAO appDAO) {
        Long courseId = 10L;
        // Cascading will delete the course and remove the association with students, but it won't delete the students themselves
        appDAO.deleteCourseById(courseId);
    }

    private void deleteStudent(AppDAO appDAO) {
        Long studentId = 2L;
        // Cascading will delete the student and remove the association with courses, but it won't delete the courses themselves
        appDAO.deleteStudentById(studentId);
    }
}
