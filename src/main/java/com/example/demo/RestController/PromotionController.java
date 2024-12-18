package com.example.demo.RestController;

import com.example.demo.Entity.Promotion;
import com.example.demo.Repository.PromotionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {
    @Autowired
    private PromotionRespository promotionRepository;

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyDiscount(@RequestBody Map<String, String> request) {
        String discountCode = request.get("discountCode");
        Map<String, Object> response = new HashMap<>();

        // Kiểm tra mã giảm giá trong cơ sở dữ liệu
        Promotion promotion = promotionRepository.findByPromotionid(discountCode);

        if (promotion != null) {
            response.put("discount", promotion.getDiscount());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid discount code");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
