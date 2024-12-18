package com.example.demo.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Customer;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Service.CustomerService;

@Service
public class CustomerServiceIMPL implements CustomerService{

      @Autowired
        private CustomerRepository customerRepository;


    @Override
    public int findCustomerIdByEmail(String email) {
        // TODO Auto-generated method stub
            Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return customer.get().getCustomerID(); // Giả sử bảng Customer có trường `id`
        } else {
            throw new UsernameNotFoundException("Customer not found with email: " + email);
        }
    }

}
