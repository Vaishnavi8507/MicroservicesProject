package com.example.Address.Repo;

import com.example.Address.Model.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Long> {
    List<Address> findAllByEmpId(Long empId);
}
