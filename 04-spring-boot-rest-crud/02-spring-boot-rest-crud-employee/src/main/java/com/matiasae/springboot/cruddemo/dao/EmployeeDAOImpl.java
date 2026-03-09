package com.matiasae.springboot.cruddemo.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import com.matiasae.springboot.cruddemo.entity.Employee;

import java.util.List;

@Repository
public class EmployeeDAOImpl implements IEmployeeDAO {
    private EntityManager entityManager;

    public EmployeeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findAll() {
        return entityManager.createQuery("from Employee", Employee.class).getResultList();
    }

    @Override
    public Employee findById(Long id) {
        return entityManager.find(Employee.class, id);
    }

    @Override
    public Employee save(Employee employee) {
        Employee dbEmployee = entityManager.merge(employee);
        return dbEmployee;
    }

    @Override
    public void deleteById(Long id) {
        Employee employee = findById(id);
        entityManager.remove(employee);
    }
}
