package com.example.Employe.Repos;

import com.example.Employe.Model.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpRepo extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmpCodeAndCompanyName(String empCode, String compName);
}
