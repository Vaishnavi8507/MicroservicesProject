package com.example.Address.Service.Impl;

import com.example.Address.Client.EmpClient;
import com.example.Address.Exception.ResourceNotFoundException;
import com.example.Address.Model.DTO.AddressDto;
import com.example.Address.Model.DTO.AddressRequest;
import com.example.Address.Model.DTO.AddressRequestDto;
import com.example.Address.Model.DTO.EmployeeDto;
import com.example.Address.Model.Entity.Address;
import com.example.Address.Repo.AddressRepo;
import com.example.Address.Service.AddressService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService {

    Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepo addressRepo;
    private final ModelMapper modelMapper;
    private final EmpClient empClient;

    public AddressServiceImpl(AddressRepo addressRepo, ModelMapper modelMapper, EmpClient empClient) {
        this.addressRepo = addressRepo;
        this.modelMapper = modelMapper;
        this.empClient = empClient;

    }

    @Override
    public List<AddressDto> saveAddress(AddressRequest addressRequest) {
        empClient.getSingleEmp(addressRequest.getEmpId());
        List<Address> listToSave = this.saveOrUpdatedAddressRequest(addressRequest);
        List<Address> savedAddress = addressRepo.saveAll(listToSave);
        return savedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public List<AddressDto> updateAddress(AddressRequest addressRequest) {
        empClient.getSingleEmp(addressRequest.getEmpId());
        List<Address> addressByEmpId = addressRepo.findAllByEmpId(addressRequest.getEmpId());
        if(addressByEmpId.isEmpty()) {
            log.info("No address found for employee id {}", addressRequest.getEmpId());
            log.info("Creating new address for employee id {}", addressRequest.getEmpId());
        }
        List<Address> listToUpdate = this.saveOrUpdatedAddressRequest(addressRequest);

        List<Long> upcomingNonNullIds = listToUpdate.stream().map(Address::getId).filter(Objects::nonNull).toList();
        List<Long> existingIds = addressByEmpId.stream().map(Address::getId).toList();
        List<Long> idsToDelete = existingIds.stream().filter(id -> !upcomingNonNullIds.contains(id)).toList();
        if(!idsToDelete.isEmpty()) {
            addressRepo.deleteAllById(idsToDelete);
        }
        List<Address> updatedAddress = addressRepo.saveAll(listToUpdate);
        return updatedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public AddressDto getSingleAddress(Long id) {
        Address address = addressRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddress() {
//        try {
//            Thread.sleep(8000); // Simulating a delay of 6 seconds
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        List<Address> all = addressRepo.findAll();
        if(all.isEmpty()) {
            throw new ResourceNotFoundException("No address found!");
        }
        return all.stream().map(address -> modelMapper.map(address,AddressDto.class)).toList();
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        addressRepo.delete(address);
    }

    @Override
    public List<AddressDto> getAddressByEmpId(Long empId) {
        List<Address> addressByEmpId = addressRepo.findAllByEmpId(empId);
        if(addressByEmpId.isEmpty()) {
            return new ArrayList<>();
        }
        return addressByEmpId.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    private List<Address> saveOrUpdatedAddressRequest(AddressRequest addressRequest) {
        List<Address> listToSave = new ArrayList<>();
        for(AddressRequestDto addressRequestDto : addressRequest.getAddressRequestDtoList()) {
            Address address = new Address();
            address.setId(addressRequestDto.getId() != null ? addressRequestDto.getId() : null);
            address.setStreet(addressRequestDto.getStreet());
            address.setCity(addressRequestDto.getCity());
            address.setCountry(addressRequestDto.getCountry());
            address.setPinCode(addressRequestDto.getPinCode());
            address.setAddressType(addressRequestDto.getAddressType());
            address.setEmpId(addressRequest.getEmpId());
            listToSave.add(address);
        }
        return listToSave;

    }
}
