package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order,Integer>{

    @Query("SELECT o FROM Order o WHERE o.customer.customerID = :customerId")
    List<Order> findByCustomerId(@Param("customerId") Integer customerId);}
