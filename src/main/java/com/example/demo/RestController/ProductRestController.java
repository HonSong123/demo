package com.example.demo.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.Entity.WarehouseReceiptDetail;
import com.example.demo.Repository.OrderDetailRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductReviews;
import com.example.demo.Repository.ProductReviewsRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Service.ProductReviewService;
import com.example.demo.Service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/sanpham")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductReviewsRepository productReviewsRepository;

    @Autowired
    private OrderDetailRespository orderDetailRespository;


    @Autowired
    private ProductReviewService service;
    

    // @Autowired
    // private SimpMessagingTemplate messagingTemplate;

    @GetMapping()
    public List<Product> getAllSanPham() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.finById(id);
    }

    // @GetMapping("/{productId}")
    // public ResponseEntity<List<ProductReviews>> getReviewsByProduct(@PathVariable Integer productId) {
    //     List<ProductReviews> reviews = service.getReviewsByProduct(productId);
    //     return ResponseEntity.ok(reviews);
    // }
    // @PostMapping("/add")
    // public ResponseEntity<ProductReviews> addReview(@RequestBody ProductReviews review) {
    //     ProductReviews savedReview = service.saveReview(review);

    //     // Gửi thông báo WebSocket đến các người dùng khác
    //     messagingTemplate.convertAndSend("/topic/reviews/" + review.getProduct().getProductID(), savedReview);

    //     return ResponseEntity.ok(savedReview);
    // }

    @GetMapping("/total-by-category")
    public ResponseEntity<Long> getTotalProductsByCategory(@RequestParam String categoryID) {
        long total = productService.getTotalProductsByCategory(categoryID);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stock/{productId}")
    public ResponseEntity<Map<String, Integer>> getProductStock(@PathVariable int productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Product product = optionalProduct.get();
        int stockQuantity = product.getWarehouseReceiptDetails().stream()
                .mapToInt(WarehouseReceiptDetail::getQuantity)
                .sum()
                - orderDetailRespository.getSoldQuantity(productId);

        Map<String, Integer> response = new HashMap<>();
        response.put("stockQuantity", stockQuantity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryID", required = false) Integer categoryID) {
        List<Product> products = productRepository.searchProducts(keyword, categoryID);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterByPrice(
            @RequestParam("minPrice") float minPrice,
            @RequestParam("maxPrice") float maxPrice) {
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }


}

