package com.matiasae.springboot.cruddemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.matiasae.springboot.cruddemo.entity.Employee;

// @RepositoryRestResource(path = "members")
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

}
