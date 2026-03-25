package com.matiasae.demo;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matiasae.demo.dao.AppDAO;
import com.matiasae.demo.entity.Course;
import com.matiasae.demo.entity.Instructor;
import com.matiasae.demo.entity.InstructorDetail;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AppDAO appDAO) {
        return runner -> {
            // ONE-TO-MANY MAPPING
            // -------------------
            // createInstructorWithCourses(appDAO);
            // findInstructorWithCourses(appDAO);
            // findCoursesForInstructor(appDAO);
            // findInstructorWithCoursesJoinFetch(appDAO);
            // updateInstructor(appDAO);
            // updateCourse(appDAO);
            // deleteInstructor(appDAO);
            deleteCourse(appDAO);
        };
    }

    private void createInstructorWithCourses(AppDAO appDAO) {
        // create the instructor
        Instructor tempInstructor = new Instructor("Susan", "Public", "susan@luv2code.com");

        // create the instructor detail
        InstructorDetail tempInstructorDetail = new InstructorDetail("http://www.youtube.com/susan", "Video Games");

        // associate the instructor with the instructor detail - (setter method in Instructor class)
        tempInstructor.setInstructorDetail(tempInstructorDetail);

        // create the courses
        Course tempCourse1 = new Course("Air Guitar - The Ultimate Guide");
        Course tempCourse2 = new Course("The Pinball Masterclass");

        // add courses to instructor
        tempInstructor.add(tempCourse1);
        tempInstructor.add(tempCourse2);

        // save the instructor
        // this will also save the courses because of CascadeType.ALL
        appDAO.save(tempInstructor);
    }

    private void findCoursesForInstructor(AppDAO appDAO) {
        Instructor tempInstructor = appDAO.findInstructorById(1L);
        System.out.println("tempInstructor: " + tempInstructor);
        System.out.println("------------------------------");
        List<Course> courses = appDAO.findCoursesByInstructorId(1L);
        tempInstructor.setCourses(courses);
        System.out.println("the associated courses: " + tempInstructor.getCourses());
    }

    // private void findInstructorWithCourses(AppDAO appDAO) {
    //     Instructor tempInstructor = appDAO.findInstructorById(1L);
    //     System.out.println("tempInstructor: " + tempInstructor);
    //     System.out.println("------------------------------");
    //     System.out.println("the associated courses: " + tempInstructor.getCourses());
    // }

    private void findInstructorWithCoursesJoinFetch(AppDAO appDAO) {
        Instructor tempInstructor = appDAO.findInstructorByIdJoinFetch(1L);
        System.out.println("tempInstructor: " + tempInstructor);
        System.out.println("------------------------------");
        System.out.println("the associated courses: " + tempInstructor.getCourses());
    }

    private void updateInstructor(AppDAO appDAO) {
        Instructor tempInstructor = appDAO.findInstructorById(1L);
        System.out.println("tempInstructor: " + tempInstructor);

        tempInstructor.setLastName("Smith");
        appDAO.update(tempInstructor);

        Instructor updatedInstructor = appDAO.findInstructorById(1L);
        System.out.println("updatedInstructor: " + updatedInstructor);
    }

    private void updateCourse(AppDAO appDAO) {
        Course tempCourse = appDAO.findCourseById(10L);
        System.out.println("tempCourse: " + tempCourse);

        tempCourse.setTitle("Air Guitar - The Ultimate Guide (Updated)");
        appDAO.update(tempCourse);

        Course updatedCourse = appDAO.findCourseById(10L);
        System.out.println("updatedCourse: " + updatedCourse);
    }

    private void deleteInstructor(AppDAO appDAO) {
        appDAO.deleteInstructorById(1L);
    }

    private void deleteCourse(AppDAO appDAO) {
        appDAO.deleteCourseById(10L);
    }
}
