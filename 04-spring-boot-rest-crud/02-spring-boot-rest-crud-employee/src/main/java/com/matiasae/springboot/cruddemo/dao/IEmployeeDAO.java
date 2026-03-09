package com.matiasae.springboot.cruddemo.dao;

import java.util.List;

import com.matiasae.springboot.cruddemo.entity.Employee;

public interface IEmployeeDAO {
    List<Employee> findAll();
    Employee findById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
}
