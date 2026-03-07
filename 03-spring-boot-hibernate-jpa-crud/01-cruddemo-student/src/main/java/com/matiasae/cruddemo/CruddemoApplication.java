package com.matiasae.cruddemo;

import org.springframework.boot.SpringApplication;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.matiasae.cruddemo.dao.IStudentDAO;
import com.matiasae.cruddemo.entity.Student;

@SpringBootApplication
public class CruddemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CruddemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(IStudentDAO  studentDAO) {
        return runner -> {
            // 1 student
            createStudent(studentDAO);

            // Multiple Students
            createMultipleStudents(studentDAO);

            // Find student by id
            Student findStudent = readStudent(studentDAO);
            System.out.println("Found student: " + findStudent);

            // Query: all students
            List<Student> students = queryForStudents(studentDAO);
            for (Student student : students) {
                System.out.println(student);
            }

            // Query: students by last name
            List<Student> studentsByLastName = queryStudentsByLastName(studentDAO, "Aguirre");
            for (Student student : studentsByLastName) {
                System.out.println(student);
            }

            // Update Student
            updateStudent(studentDAO, 1);

            // Delete Student
            // deleteStudent(studentDAO, 2);
            // Student deletedStudent = studentDAO.findById(2);
            // System.out.println("Deleted student found: " + deletedStudent);

            // Delete all students
            // System.out.println("Deleting all students...");
            // int rowsDeleted = studentDAO.deleteAll();
            // System.out.println("Deleted " + rowsDeleted + " students");
        };
    }

    private void createStudent(IStudentDAO studentDAO) {
        System.out.println("Creating new student object...");
        Student student = new Student("Matias", "Zor", "matias@example.com");
        studentDAO.save(student);
        System.out.println("Saved student. Generated id: " + student.getId());
    }

    private void createMultipleStudents(IStudentDAO studentDAO) {
        System.out.println("Creating 3 student objects...");
        Student student1 = new Student("Ken", "Baez", "ken@example.com");
        Student student2 = new Student("John", "Aguirre", "john@example.com");
        Student student3 = new Student("Mary", "Afonso", "mary@example.com");

        System.out.println("Saving the students...");
        studentDAO.save(student1);
        studentDAO.save(student2);
        studentDAO.save(student3);
        System.out.println("Saved students. Generated ids: " + student1.getId() + ", " + student2.getId() + ", " + student3.getId());
    }

    private Student readStudent(IStudentDAO studentDAO) {
        int id = 2;
        System.out.println("Retrieving student with id: " + id);
        Student student = studentDAO.findById(id);
        return student;
    }

    private List<Student> queryForStudents(IStudentDAO studentDAO) {
        System.out.println("Querying for all students...");
        List<Student> students = studentDAO.findAll();
        return students;
    }

    private List<Student> queryStudentsByLastName(IStudentDAO studentDAO, String lastName) {
        System.out.println("Querying for students with last name: " + lastName);
        List<Student> students = studentDAO.findByLastName(lastName);
        return students;
    }

    private void updateStudent(IStudentDAO studentDAO, int id) {
        System.out.println("Updating student with id: " + id);
        Student student = studentDAO.findById(id);
        if (student != null) {
            student.setFirstName("UpdatedName");
            studentDAO.update(student);
            System.out.println("Updated student: " + student);
        } else {
            System.out.println("Student not found with id: " + id);
        }
    }

    private void deleteStudent(IStudentDAO studentDAO, int id) {
        System.out.println("Deleting student with id: " + id);
        studentDAO.deleteById(id);
        System.out.println("Deleted student with id: " + id);
    }
}
