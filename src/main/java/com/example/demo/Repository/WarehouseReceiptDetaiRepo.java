package com.example.demo.Repository;

import com.example.demo.Entity.WarehouseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseReceiptDetaiRepo extends JpaRepository<WarehouseReceipt, Integer> {
}
