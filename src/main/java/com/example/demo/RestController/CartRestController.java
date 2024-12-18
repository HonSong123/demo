package com.example.demo.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Request.ResponseMessage;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRespository orderDetailRespository;

    @GetMapping("/details/{customerId}")
    public ResponseEntity<List<CartDetailDTO>> getCartDetails(@PathVariable("customerId") Integer customerId) {
        List<CartDetailDTO> cartDetails = cartDetailRepository.findCartDetailsByCustomerId(customerId);
        if (cartDetails.isEmpty()) {
            return ResponseEntity.ok().body(cartDetails); // Return an empty list if there are no items
        }
        return ResponseEntity.ok(cartDetails);
    }

    @PostMapping("/add/{customerId}")
    public ResponseEntity<Object> addToCart(@PathVariable Integer customerId, @RequestBody CartDetailDTO cartDetailDTO) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (cartDetailDTO.getProductID() == 0 || cartDetailDTO.getQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid product data."));
            }
    
            // Tìm khách hàng
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
    
            // Tìm hoặc tạo giỏ hàng cho khách hàng
            Cart cart = cartRepository.findByCustomer_CustomerID(customerId)
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        newCart.setCustomer(customer);
                        return cartRepository.save(newCart);
                    });
    
            // Tìm sản phẩm theo ID
            Product product = productRepository.findById(cartDetailDTO.getProductID())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
    
            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            Optional<CartDetail> existingCartDetailOpt = cartDetailRepository.findByGioHangAndSanPham(cart, product);
            if (existingCartDetailOpt.isPresent()) {
                // Nếu có, cập nhật số lượng sản phẩm
                CartDetail existingCartDetail = existingCartDetailOpt.get();
                existingCartDetail.setQuantity(existingCartDetail.getQuantity() + cartDetailDTO.getQuantity());
                cartDetailRepository.save(existingCartDetail);
                return ResponseEntity.ok(new ResponseMessage("Product quantity updated in the cart"));
            } else {
                // Nếu không có, thêm sản phẩm mới vào giỏ hàng
                CartDetail newCartDetail = new CartDetail(customerId, cart, product, cartDetailDTO.getQuantity());
                cartDetailRepository.save(newCartDetail);
                return ResponseEntity.ok(new ResponseMessage("Product added to cart"));
            }
    
        } catch (RuntimeException e) {
            // Lỗi tìm khách hàng hoặc sản phẩm
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        } catch (Exception e) {
            // Lỗi hệ thống hoặc lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("An error occurred: " + e.getMessage()));
                    
        }
    }
    


    @PostMapping("/update")
    public ResponseEntity<ResponseMessage> updateCartItem(@RequestBody CartItemUpdateRequest request) {
        // Retrieve the Cart object using customerId
        Integer customerId = request.getCustomerId();
        Optional<Cart> optionalCart = cartRepository.findByCustomer_CustomerID(customerId);

        // Check if the Cart exists
        if (!optionalCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Cart not found."));
        }

        Cart cart = optionalCart.get(); // Unwrap the Optional<Cart>

        // Retrieve the product using productId
        int productId = request.getProductId();
        Optional<Product> optionalProduct = productRepository.findById((int) productId);

        // Check if the Product exists
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Product not found."));
        }
        Product product = optionalProduct.get();

        int stockQuantity = product.getWarehouseReceiptDetails().stream()
                .mapToInt(WarehouseReceiptDetail::getQuantity)
                .sum()
                - orderDetailRespository.getSoldQuantity(productId);

        if (request.getQuantity() > stockQuantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Hàng trong kho đã hết."));
        }

        // Find the CartDetail using the Cart and Product
        Optional<CartDetail> cartDetailOptional = cartDetailRepository.findByGioHangAndSanPham(cart, product);

        // Check if the CartDetail exists
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();
            cartDetail.setQuantity(request.getQuantity()); // Update the quantity

            // Optionally, update the total price based on the new quantity
            float totalPrice = product.getPrice() * cartDetail.getQuantity();
            // cartDetail.setTotalPrice(totalPrice); // If you want to store totalPrice

            cartDetailRepository.save(cartDetail); // Save the updated CartDetail

            return ResponseEntity.ok(new ResponseMessage("Cart item updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Cart item not found."));
        }
    }

    @DeleteMapping("/remove/{productId}")
public ResponseEntity<Map<String, String>> removeCartItem(@PathVariable int productId, @RequestParam Integer customerId) {
    // Lấy Cart của khách hàng dựa trên customerId
    Optional<Cart> optionalCart = cartRepository.findByCustomer_CustomerID(customerId);

    if (!optionalCart.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Cart not found for customer."));
    }

    Cart cart = optionalCart.get();

    // Tìm CartDetail dựa trên Cart và Product
    Optional<CartDetail> optionalCartDetail = cartDetailRepository.findByGioHangAndProductId(cart, productId);

    if (!optionalCartDetail.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Product not found in cart."));
    }

    CartDetail cartDetail = optionalCartDetail.get();
    cartDetailRepository.delete(cartDetail); // Xóa sản phẩm khỏi giỏ hàng

    return ResponseEntity.ok(Map.of("message", "Product removed from cart"));
}


}

    


