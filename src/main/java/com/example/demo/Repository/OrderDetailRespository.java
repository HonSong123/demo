package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.OrderDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRespository  extends JpaRepository<OrderDetail,Integer>{

    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od WHERE od.product.productID = :productId")
    int getSoldQuantity(@Param("productId") int productId);

    @Query("SELECT od FROM OrderDetail od WHERE od.orders.orderid = :orderId")
    List<OrderDetail> findByOrderId(Integer orderId);
}
