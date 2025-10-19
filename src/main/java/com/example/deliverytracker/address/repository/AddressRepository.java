package com.example.deliverytracker.address.repository;

import com.example.deliverytracker.address.enitity.Address;
import com.example.deliverytracker.user.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(User user);
}