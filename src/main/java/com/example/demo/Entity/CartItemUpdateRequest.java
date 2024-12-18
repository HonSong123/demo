package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemUpdateRequest {
    private int productId; // The product ID for the cart item
    private int quantity;
    private Integer customerId;
}
