package com.example.Address.Client;

import com.example.Address.Model.DTO.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Employe", path = "/employees")
public interface EmpClient {

    @GetMapping("/{id}")
    EmployeeDto getSingleEmp(@PathVariable("id") Long id);
}
