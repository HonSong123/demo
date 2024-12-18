package com.example.demo.Request;

import java.util.List;

import com.example.demo.Entity.Cart;

import jakarta.annotation.sql.DataSourceDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    private Integer customerId;
    private String username;
    private Integer role;
    private List<Cart> carts;
    public LoginResponse(Integer customerId, String username, Integer role, Cart cart) {
        this.customerId = customerId;
        this.username = username;
        this.role = role;
        this.carts = carts;
    }
  
}

