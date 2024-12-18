package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.Cart;
import com.example.demo.Entity.Customer;

public interface CartRepository extends JpaRepository<Cart, Long>{
    Cart findByCustomer(Customer khachHang);

    Optional<Cart> findByCustomer_CustomerID(Integer customerId);

   @Query("SELECT c FROM Cart c WHERE c.customer.customerID = :customerId")
    Cart findByCustomerId(@Param("customerId") Integer customerId);


    // Optional<Cart> findByCustomerId2(Long customerId);

    void deleteByCustomer_CustomerID(Integer customerId);

    

}
