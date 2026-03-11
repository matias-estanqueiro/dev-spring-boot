package com.matiasae.springboot.cruddemo.service;

import java.util.List;
import java.util.Map;

import com.matiasae.springboot.cruddemo.entity.Employee;

public interface IEmployeeService {
    List<Employee> findAll();
    Employee findById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
    Employee updateEmployeePartial(Long id, Map<String, Object> updates);
}
