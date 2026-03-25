package com.matiasae.demo.dao;

import org.springframework.stereotype.Repository;

import com.matiasae.demo.entity.Instructor;
import com.matiasae.demo.entity.InstructorDetail;

import jakarta.persistence.EntityManager;
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

    @Transactional
    @Override
    public void deleteInstructorById(Long id) {
        Instructor tempInstructor = entityManager.find(Instructor.class, id);
        if (tempInstructor != null) {
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
}
