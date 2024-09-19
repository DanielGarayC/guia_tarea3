package com.example.tarea2_20212591_20212474.repository;

import com.example.tarea2_20212591_20212474.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    @Query("SELECT e FROM Employee e WHERE e.id IN (SELECT DISTINCT manager.id FROM Employee WHERE manager.id IS NOT NULL)")
    List<Employee> findManagers();
    @Query("SELECT e FROM Employee e " +
            "WHERE LOWER(e.firstName) LIKE %:search% " +
            "OR LOWER(e.lastName) LIKE %:search% " +
            "OR LOWER(e.job.jobTitle) LIKE %:search% " +
            "OR LOWER(e.department.departmentName) LIKE %:search% " +
            "OR LOWER(e.department.location.city) LIKE %:search%")
    List<Employee> findBySearchCriteria(@Param("search") String search);
}
