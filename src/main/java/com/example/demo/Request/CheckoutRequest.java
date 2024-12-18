    package com.example.demo.Request;

import java.util.List;

import com.example.demo.Entity.CartDetail;
import com.example.demo.Entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private Integer customerId;
    private String paymentMethod;
    private Double totalAmount;
    private List<OrderDetailRequest> orderDetails;

}
