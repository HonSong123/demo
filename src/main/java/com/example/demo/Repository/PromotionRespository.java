package com.example.demo.Repository;

import com.example.demo.Entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRespository extends JpaRepository<Promotion, String> {
    Promotion findByPromotionid(String discountCode);
}
