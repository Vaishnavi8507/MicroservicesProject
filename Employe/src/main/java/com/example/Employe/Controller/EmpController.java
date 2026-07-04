package com.example.Employe.Controller;

import com.example.Employe.Exception.MissingParamException;
import com.example.Employe.Model.DTO.EmployeeDto;
import com.example.Employe.Service.EmpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmpController {
    private final EmpService empService;

    public EmpController(EmpService empService) {
        this.empService = empService;
    }

    @PostMapping("/save")
    public ResponseEntity<EmployeeDto> saveEmp(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto response = empService.saveEmp(employeeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDto> updateEmp(@RequestBody EmployeeDto employeeDto, @PathVariable Long id) {
        EmployeeDto response = empService.updateEmp(id, employeeDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmp(@PathVariable Long id) {
        empService.deleteEmp(id);
        return new ResponseEntity<>("Employee deleted !!",HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getSingleEmp(@PathVariable Long id) {
        EmployeeDto response = empService.getSingleEmp(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<EmployeeDto>> getAllEmp() {
        Iterable<EmployeeDto> response = empService.getAllEmp();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-by-emp-code-and-company-name")
    public ResponseEntity<EmployeeDto> findByEmpCodeAndCompanyName(@RequestParam(required = false) String empCode, @RequestParam(required = false) String companyName) {
        List<String> missingParam = new ArrayList<>();
        if(empCode == null || empCode.trim().isEmpty()) {
            missingParam.add("empCode");
     }
        if(companyName == null || companyName.trim().isEmpty()) {
            missingParam.add("companyName");
        }
        if(!missingParam.isEmpty()) {
            String finalMsg = missingParam.stream().collect(Collectors.joining(","));
            throw new MissingParamException("Please provide" + finalMsg);
        }
        EmployeeDto response = empService.getEmpByCodeNCompName(empCode, companyName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
