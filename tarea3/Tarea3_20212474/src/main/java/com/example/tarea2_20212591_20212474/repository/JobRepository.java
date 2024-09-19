package com.example.tarea2_20212591_20212474.repository;

import com.example.tarea2_20212591_20212474.entity.Employee;
import com.example.tarea2_20212591_20212474.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job,String> {
}
