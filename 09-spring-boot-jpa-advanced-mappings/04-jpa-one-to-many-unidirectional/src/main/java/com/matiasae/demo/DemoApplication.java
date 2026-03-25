package com.matiasae.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matiasae.demo.dao.AppDAO;
import com.matiasae.demo.entity.Course;
import com.matiasae.demo.entity.Review;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AppDAO appDAO) {
        return runner -> {
            // ONE-TO-MANY UNI MAPPING
            // -------------------
            // createCourseAndReviews(appDAO);
            // retrieveCourseAndReviews(appDAO);
            deleteCourseAndReviews(appDAO);
        };
    }

    private void createCourseAndReviews(AppDAO appDAO) {
        // create a course
        Course tempCourse = new Course("Pacman - How To Score One Million Points");

        // add some reviews
        tempCourse.addReview(new Review("Great course ... loved it!"));
        tempCourse.addReview(new Review("Cool course, job well done"));
        tempCourse.addReview(new Review("What a dumb course, you are an idiot!"));

        // save the course (cascade ALL will save the reviews)
        System.out.println("Saving the course");
        System.out.println(tempCourse);
        System.out.println(tempCourse.getReviews());
        appDAO.save(tempCourse);
    }

    private void retrieveCourseAndReviews(AppDAO appDAO) {
        Course tempCourse = appDAO.findCourseByIdWithReviews(10L);

        System.out.println("Course: " + tempCourse);
        System.out.println("Reviews: " + tempCourse.getReviews());
    }

    private void deleteCourseAndReviews(AppDAO appDAO) {
        // cascade ALL will delete the reviews when the course is deleted 
        appDAO.deleteCourseById(10L);
    }
}
