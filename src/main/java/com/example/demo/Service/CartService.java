package com.example.demo.Service;

import java.util.List;

import com.example.demo.Entity.Cart;
import com.example.demo.Entity.CartDetailDTO;
import com.example.demo.Entity.Customer;

public interface CartService {
    public List<CartDetailDTO> getChiTietGioHang(Cart cart) ;
    public Cart getGioHangForKhachHang(Customer customer);
    
}
