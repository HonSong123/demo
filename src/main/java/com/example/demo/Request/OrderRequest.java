package com.example.demo.Request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Integer customerId;
    private String paymentMethod;
    private Double totalAmount;
    private List<OrderDetailRequest> orderDetails;
    private Long cartId;  // Thêm trường cartId

}
