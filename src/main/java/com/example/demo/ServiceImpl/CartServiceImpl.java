package com.example.demo.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Cart;
import com.example.demo.Entity.CartDetailDTO;
import com.example.demo.Entity.Customer;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.CartDetailRepository;

import com.example.demo.Service.CartService;

@Service
public class CartServiceImpl implements CartService{

    private final CartDetailRepository cartDetailRepository;

    @Autowired // Inject the repository
    public CartServiceImpl(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }
    @Autowired
    private CartRepository gioHangRepository;

    @Autowired
    private CustomerRepository khachHangRepository;

    @Override
    public Cart getGioHangForKhachHang(Customer customer) {
        return gioHangRepository.findByCustomer(customer);
    }

    @Override
    public List<CartDetailDTO> getChiTietGioHang(Cart cart) {
        return cartDetailRepository.findCartDetailsByCustomerId(cart.getCustomer().getCustomerID());
    }

   
}
