package com.matiasae.springboot.cruddemo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.matiasae.springboot.cruddemo.dao.IEmployeeRepository;
import com.matiasae.springboot.cruddemo.entity.Employee;
import com.matiasae.springboot.cruddemo.exception.EmployeeBadRequestException;
import com.matiasae.springboot.cruddemo.exception.EmployeeNotFoundException;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    private IEmployeeRepository employeeRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    public EmployeeServiceImpl(IEmployeeRepository employeeRepository, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        if(id == null) {
            throw new EmployeeBadRequestException("Employee id cannot be null");
        }
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee save(Employee employee) {
        if (employee == null) {
            throw new EmployeeBadRequestException("Employee cannot be null");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        if(id == null) {
            throw new EmployeeBadRequestException("Employee id cannot be null");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee updateEmployeePartial(Long id, Map<String, Object> updates) {
        if(id == null) {
            throw new EmployeeBadRequestException("Employee id cannot be null");
        }
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
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
        return employeeRepository.save(employee);
    }
}
