package com.example.Employe.Service.Impl;

import com.example.Employe.Client.AddressClient;
import com.example.Employe.Exception.BadRequestException;
import com.example.Employe.Exception.ResourceNotFoundException;
import com.example.Employe.Model.DTO.AddressDto;
import com.example.Employe.Model.DTO.EmployeeDto;
import com.example.Employe.Model.Entity.Employee;
import com.example.Employe.Repos.EmpRepo;
import com.example.Employe.Service.EmpService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmpServiceImpl implements EmpService {

    private final EmpRepo empRepo;
    private final ModelMapper modelMapper;
    private final AddressClient addressClient;

    public EmpServiceImpl(EmpRepo empRepo, ModelMapper modelMapper, AddressClient addressClient) {
        this.empRepo = empRepo;
        this.modelMapper = modelMapper;
        this.addressClient = addressClient;
    }

    @Override
    public EmployeeDto saveEmp(EmployeeDto employeeDto) {
        if(employeeDto.getId() != null) {
            throw new BadRequestException("Employee already exits", HttpStatus.SERVICE_UNAVAILABLE);
        }
        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee savedEntity = empRepo.save(entity);
        return modelMapper.map(savedEntity, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmp(Long id, EmployeeDto employeeDto) {
        if(id == null || employeeDto.getId() == null) {
            throw new BadRequestException("Enter employee id", HttpStatus.SERVICE_UNAVAILABLE);
        }
        if(!Objects.equals(id, employeeDto.getId())) {
            throw new BadRequestException("Id mismatched", HttpStatus.SERVICE_UNAVAILABLE);
        }
        empRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee updatedEntity = empRepo.save(entity);
        return modelMapper.map(updatedEntity, EmployeeDto.class);
    }

    @Override
    public void deleteEmp(Long id) {
        Employee employee = empRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        empRepo.delete(employee);

    }

    @Override
    public EmployeeDto getSingleEmp(Long id) {
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        Employee employee= empRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        List<AddressDto> addresses = new ArrayList<>();
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);

        try{
            addresses = addressClient.getAddressByEmpId(id);
            dto.setAddress(addresses);
        } catch (Exception e) {
            // If addresses service is unavailable or employee has no addresses, continue with empty list
            dto.setAddress(new ArrayList<>());
        }
        return dto;
    }

    @Override
    public List<EmployeeDto> getAllEmp() {
        List<Employee> employees = empRepo.findAll();
        if(employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found");
        }
        List<EmployeeDto> employeDtoList = employees.stream().map(emp -> modelMapper.map(emp, EmployeeDto.class)).toList();
        List<EmployeeDto> response = new ArrayList<>();
        for(EmployeeDto employeeDto: employeDtoList) {
            List<AddressDto> addresses = new ArrayList<>();
            try{
                addresses = addressClient.getAddressByEmpId(employeeDto.getId());
                employeeDto.setAddress(addresses);
            } catch (Exception e) {
                // If addresses service is unavailable or employee has no addresses, continue with empty list
                employeeDto.setAddress(new ArrayList<>());
            }
            response.add(employeeDto);
        }
        return response;
    }

    @Override
    public EmployeeDto getEmpByCodeNCompName(String empCode, String companyName) {
        Employee employee = empRepo.findByEmpCodeAndCompanyName(empCode, companyName).orElseThrow(() -> new ResourceNotFoundException("Employee not found with EmpCode and compName"));
        return modelMapper.map(employee, EmployeeDto.class);
    }
}
