package com.example.tarea2_20212591_20212474.repository;

import com.example.tarea2_20212591_20212474.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
}
