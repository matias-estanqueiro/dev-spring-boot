package com.matiasae.springboot.cruddemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matiasae.springboot.cruddemo.entity.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

}
