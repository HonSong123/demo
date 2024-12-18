package com.example.demo.RestController;

import java.util.*;

import com.example.demo.Entity.*;
import com.example.demo.Repository.CartDetailRepository;
import com.example.demo.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.OrderDetailRespository;
import com.example.demo.Repository.OrdersRepository;
import com.example.demo.Request.CheckoutRequest;
import com.example.demo.Request.OrderDetailRequest;


@RestController
@RequestMapping("/api/checkout")
public class OrderController {

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private OrderDetailRespository orderDetailRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping("/process")
    public ResponseEntity<?> processCheckout(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            // Lấy thông tin khách hàng và giỏ hàng
            Customer customer = customerRepository.findById(checkoutRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            Cart cart = customer.getCart();

            // Tạo đơn hàng
            Order order = new Order();
            order.setPaymentmethod(checkoutRequest.getPaymentMethod());
            order.setCreateDate(new Date());
            order.setTotalamount(checkoutRequest.getTotalAmount());
            order.setCustomer(customer);
            order.setCart(cart);

            // Thiết lập paymentStatus dựa trên paymentMethod
            if ("Trả tiền khi nhận hàng".equalsIgnoreCase(checkoutRequest.getPaymentMethod())) {
                order.setPaymentstatus("Chưa thanh toán");
            } else if ("Thanh toán bằng thẻ tín dụng".equalsIgnoreCase(checkoutRequest.getPaymentMethod())) {
                order.setPaymentstatus("Đã thanh toán");
            } else {
                order.setPaymentstatus("Không xác định");
            }
            order.setDeliverystatus("Processing");
            orderRepository.save(order);

            // Tạo chi tiết đơn hàng
            for (OrderDetailRequest item : checkoutRequest.getOrderDetails()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrders(order);
                orderDetail.setProduct(item.getProduct());
                orderDetail.setQuantity(item.getQuantity());
                orderDetailRepository.save(orderDetail);
            }

            // Xóa các sản phẩm được chọn để thanh toán khỏi giỏ hàng
            for (OrderDetailRequest item : checkoutRequest.getOrderDetails()) {
                Product product = item.getProduct();
                Optional<CartDetail> cartDetailOpt = cartDetailRepository.findByGioHangAndSanPham(cart, product);
                cartDetailOpt.ifPresent(cartDetailRepository::delete);
            }

            // Trả về phản hồi
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Order processed successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing order: " + e.getMessage());
        }
    }

    //    @GetMapping()
//    public Order getOrder(@PathVariable Integer orderId) {
//        return orderService.getOrderWithDetails(orderId);
//    }
@GetMapping("/customer/{customerId}")
public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Integer customerId) {
    List<Order> orders = orderRepository.findByCustomerId(customerId);
    if (orders.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(orders);
}


    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId) {
        try {
            // Kiểm tra xem đơn hàng có tồn tại không
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Xóa đơn hàng
            orderRepository.delete(order);

            // Phản hồi thành công
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Order has been canceled successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error canceling order: " + e.getMessage());
        }
    }

}
