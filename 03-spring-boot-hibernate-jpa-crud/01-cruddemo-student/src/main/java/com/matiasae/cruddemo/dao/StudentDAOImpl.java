package com.matiasae.cruddemo.dao;
import com.matiasae.cruddemo.entity.Student;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class StudentDAOImpl implements IStudentDAO {
    private EntityManager entityManager;

    // @Autowired
    public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    @Transactional
    public void save(Student student) {
        entityManager.persist(student);
    }

    @Override
    public Student findById(Integer id) {
        return entityManager.find(Student.class, id);
    }

    @Override
    public java.util.List<Student> findAll() {
        return entityManager.createQuery("FROM Student", Student.class).getResultList();
    }

    @Override
    public java.util.List<Student> findByLastName(String lastName) {
        return entityManager.createQuery("FROM Student WHERE lastName=:theData", Student.class)
            .setParameter("theData", lastName)
            .getResultList();
    }

    @Override
    @Transactional
    public Student update(Student student) {
        return entityManager.merge(student);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        Student student = findById(id);
        if (student != null) {
            entityManager.remove(student);
        }
    }

    @Override
    @Transactional
    public Integer deleteAll() {
        return entityManager.createQuery("DELETE FROM Student").executeUpdate();
    }

}
