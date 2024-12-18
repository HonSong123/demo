 package com.example.demo.Service;

 import java.util.ArrayList;
 import java.util.Date;
 import java.util.List;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import com.example.demo.Entity.Cart;
 import com.example.demo.Entity.CartDetail;
 import com.example.demo.Entity.Customer;
 import com.example.demo.Entity.Order;
 import com.example.demo.Entity.OrderDetail;
 import com.example.demo.Entity.Product;
 import com.example.demo.Repository.CartRepository;
 import com.example.demo.Repository.CustomerRepository;

 import com.example.demo.Repository.OrderDetailRespository;
 import com.example.demo.Repository.OrdersRepository;

 import com.example.demo.Request.CheckoutRequest;


 import jakarta.transaction.Transactional;

 @Service
 public class OrderService {
     @Autowired
     private OrdersRepository orderRepository;

     @Autowired
     private OrderDetailRespository orderDetailRepository;

     public Order getOrderWithDetails(Integer orderId) {
         // Lấy đơn hàng
         Order order = orderRepository.findById(orderId)
                 .orElseThrow(() -> new RuntimeException("Order not found"));

         // Lấy chi tiết đơn hàng và gán vào Order
         List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
         order.setOrderDetails(orderDetails);

         return order;
     }

     public List<Order> getAllOrders() {
         // Lấy tất cả các đơn hàng
         return orderRepository.findAll();
     }
 }