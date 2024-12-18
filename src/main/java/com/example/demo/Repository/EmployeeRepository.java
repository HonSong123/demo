package com.example.demo.Repository;

import com.example.demo.Entity.Account;
import com.example.demo.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByAccount(Account account);
}
