package com.matiasae.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matiasae.demo.dao.AppDAO;
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
            // createInstructor(appDAO);
            // findInstructor(appDAO);
            // deleteInstructor(appDAO);
            // findInstructorDetail(appDAO);
            deleteInstructorDetail(appDAO);
        };
    }

    private void createInstructor(AppDAO appDAO) {
        Instructor tempInstructor = new Instructor("Chad", "Darby", "darby@luv2code.com");
        InstructorDetail tempInstructorDetail = new InstructorDetail("http://www.luv2code.com/youtube", "Luv 2 code!!!");

        // Instructor tempInstructor = new Instructor("Madhu", "Patel", "madhu@luv2code.com");
        // InstructorDetail tempInstructorDetail = new InstructorDetail("http://www.luv2code.com/youtube", "Guitar");

        // associate the objects
        tempInstructor.setInstructorDetail(tempInstructorDetail);

        // save the instructor
        // this will also save the details object because of CascadeType.ALL
        appDAO.save(tempInstructor);
    }

    private void findInstructor(AppDAO appDAO) {
        Instructor tempInstructor = appDAO.findInstructorById(2L);
        System.out.println("tempInstructor: " + tempInstructor);
        System.out.println("------------------------------");
        System.out.println("the associated instructorDetail: " + tempInstructor.getInstructorDetail());
    }

    private void deleteInstructor(AppDAO appDAO) {
        appDAO.deleteInstructorById(1L);
    }

    private void findInstructorDetail(AppDAO appDAO) {
        InstructorDetail tempInstructorDetail = appDAO.findInstructorDetailById(2L);
        System.out.println("tempInstructorDetail: " + tempInstructorDetail);
        System.out.println("------------------------------");
        System.out.println("the associated instructor: " + tempInstructorDetail.getInstructor());
    }

    private void deleteInstructorDetail(AppDAO appDAO) {
        InstructorDetail tempInstructorDetail = appDAO.deleteInstructorDetailById(3L);
        System.out.println("tempInstructorDetail: " + tempInstructorDetail);
        System.out.println("------------------------------");
        System.out.println("the associated instructor: " + tempInstructorDetail.getInstructor());
    }
}
