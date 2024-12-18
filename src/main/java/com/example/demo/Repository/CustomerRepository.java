package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Account;
import com.example.demo.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    Customer findByAccount(Account account);
    // CustomerRepository.java
Optional<Customer> findByEmail(String email);


}
