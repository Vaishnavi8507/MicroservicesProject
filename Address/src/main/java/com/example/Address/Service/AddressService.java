package com.example.Address.Service;

import com.example.Address.Model.DTO.AddressDto;
import com.example.Address.Model.DTO.AddressRequest;

import java.util.List;

public interface AddressService {
    List<AddressDto> saveAddress(AddressRequest addressRequest);

    List<AddressDto> updateAddress(AddressRequest addressRequest);

    AddressDto getSingleAddress(Long id);

    List<AddressDto> getAllAddress();

    void deleteAddress(Long id);

    List<AddressDto> getAddressByEmpId(Long empId);
}
