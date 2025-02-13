package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Account;

public interface AccountRepository extends JpaRepository<Account,Long>{
    Optional<Account> findByUsername(String username);


}
