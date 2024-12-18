package com.example.demo.Request;

import com.example.demo.Entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {
   private Product product;
    private Integer quantity;

}
