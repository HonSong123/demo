package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.CartDetailDTO;
import com.example.demo.Entity.Product;

import jakarta.transaction.Transactional;

import com.example.demo.Entity.Cart;
import com.example.demo.Entity.CartDetail;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long>{
    

    @Query("SELECT new com.example.demo.Entity.CartDetailDTO(sp.productname, sp.productID, sp.price, MIN(asp.image), ghct.quantity) " +
    "FROM CartDetail ghct " +
    "JOIN ghct.cart gh " +
    "JOIN gh.customer kh " +
    "JOIN ghct.product sp " +
    "LEFT JOIN sp.productimage asp " +
    "WHERE kh.customerID = :customerID " +
    "GROUP BY sp.productname, sp.productID, sp.price, ghct.quantity")
List<CartDetailDTO> findCartDetailsByCustomerId(@Param("customerID") Integer customerID);


 @Query("SELECT ghct FROM CartDetail ghct WHERE ghct.cart = :gioHang AND ghct.product = :sanPham")
    Optional<CartDetail> findByGioHangAndSanPham(@Param("gioHang") Cart gioHang, @Param("sanPham") Product sanPham);

    @Query("SELECT ghct FROM CartDetail ghct WHERE ghct.cart = :gioHang AND ghct.product.productID = :productId")
    Optional<CartDetail> findByGioHangAndProductId(@Param("gioHang") Cart gioHang, @Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartDetail cd WHERE cd.cart = :cart AND cd.product = :product")
    void deleteByCartAndProduct(@Param("cart") Cart cart, @Param("product") Product product);

}






