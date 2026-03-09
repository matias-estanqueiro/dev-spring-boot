package com.matiasae.springboot.cruddemo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.matiasae.springboot.cruddemo.dao.IEmployeeDAO;
import com.matiasae.springboot.cruddemo.entity.Employee;
import com.matiasae.springboot.cruddemo.exception.EmployeeBadRequestException;
import com.matiasae.springboot.cruddemo.exception.EmployeeNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private IEmployeeDAO employeeDAO;
    private ObjectMapper objectMapper = new ObjectMapper();

    public EmployeeServiceImpl(IEmployeeDAO employeeDAO, ObjectMapper objectMapper) {
        this.employeeDAO = employeeDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return employeeDAO.findById(id);
    }

    @Transactional
    @Override
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        employeeDAO.deleteById(id);
    }

    @Transactional
    @Override
    public Employee updateEmployeePartial(Long id, Map<String, Object> updates) {
        Employee employee = employeeDAO.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        if (updates.containsKey("id")) {
            throw new EmployeeBadRequestException("Employee id cannot be updated");
        }
        try {
            objectMapper.updateValue(employee, updates);
        } catch (Exception e) {
            throw new EmployeeBadRequestException("Invalid employee data format: " + e.getMessage());
        }
        return employeeDAO.save(employee);
    }
}
