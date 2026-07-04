package com.example.Employe.Service;

import com.example.Employe.Model.DTO.EmployeeDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmpService {
    EmployeeDto saveEmp(EmployeeDto employeeDto);

    EmployeeDto updateEmp(Long id, EmployeeDto employeeDto);

    void deleteEmp(Long id);

    EmployeeDto getSingleEmp(Long id);

    List<EmployeeDto> getAllEmp();

    EmployeeDto getEmpByCodeNCompName(String empCode, String companyName);

}

