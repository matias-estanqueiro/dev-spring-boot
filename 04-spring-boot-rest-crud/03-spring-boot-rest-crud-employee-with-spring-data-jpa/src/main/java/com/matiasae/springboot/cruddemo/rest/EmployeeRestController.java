package com.matiasae.springboot.cruddemo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matiasae.springboot.cruddemo.entity.Employee;
import com.matiasae.springboot.cruddemo.exception.EmployeeNotFoundException;
import com.matiasae.springboot.cruddemo.service.IEmployeeService;


@RestController
@RequestMapping("/api")
public class EmployeeRestController {
    private IEmployeeService employeeService;

    public EmployeeRestController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        employee.setId(null);
        return employeeService.save(employee);
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) {
        if (employeeService.findById(employee.getId()) == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employee.getId());
        }
        return employeeService.save(employee);
    }

    @GetMapping("/employees/{employeeId}")
    public Employee findById(@PathVariable Long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        return employee;
    }

    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable Long employeeId, @RequestBody Map<String, Object> employeeJson) {
        return employeeService.updateEmployeePartial(employeeId, employeeJson);
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable Long employeeId) {
        if (employeeService.findById(employeeId) == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        employeeService.deleteById(employeeId);
        return "Deleted employee with id: " + employeeId;
    }
}
