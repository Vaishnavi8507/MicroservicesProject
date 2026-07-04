package com.example.Employe.Client;

import com.example.Employe.Model.DTO.AddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "Address", path = "/addresses")
public interface AddressClient {

    @GetMapping("/empId/{empId}")
    List<AddressDto> getAddressByEmpId(@PathVariable("empId") Long empId);

}
