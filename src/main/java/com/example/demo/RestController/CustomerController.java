package com.example.demo.RestController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Customer;
import com.example.demo.Repository.CustomerRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @PutMapping("/update")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCustomer) {
        if (updatedCustomer.getCustomerID() != null && customerRepository.existsById(updatedCustomer.getCustomerID())) {
            // Lấy thông tin khách hàng hiện tại từ cơ sở dữ liệu
            Customer existingCustomer = customerRepository.findById(updatedCustomer.getCustomerID()).orElse(null);
            if (existingCustomer == null) {
                return ResponseEntity.notFound().build();
            }

            // Chỉ cập nhật các trường cần thiết
            existingCustomer.setCustomername(updatedCustomer.getCustomername());
            existingCustomer.setBirthdate(updatedCustomer.getBirthdate());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setPhone(updatedCustomer.getPhone());
            existingCustomer.setGender(updatedCustomer.getGender());
            existingCustomer.setAddress(updatedCustomer.getAddress());

            // Lưu thông tin khách hàng đã chỉnh sửa
            Customer savedCustomer = customerRepository.save(existingCustomer);

            return ResponseEntity.ok(savedCustomer);
        }
        return ResponseEntity.badRequest().build();
    }

}