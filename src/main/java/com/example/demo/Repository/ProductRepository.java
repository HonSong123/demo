package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

     @Query("SELECT a.image FROM ProductImage a WHERE a.product.productID = :productID")
    List<String> findImagesByProductId(@Param("productID") int productID);


    @Query("SELECT p FROM Product p WHERE p.category.categoryID=?1")
	List<Product> findByCategoryId(String categoryID);

    Optional<Product> findById(int id);

    List<Product> findByPriceBetween(float minPrice, float maxPrice);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.categoryID = :categoryID")
    long countByCategory(@Param("categoryID") String categoryID);

    @Query("SELECT p FROM Product p WHERE " +
            "(LOWER(p.productname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR :keyword IS NULL) " +
            "AND (p.category.categoryID = :categoryID OR :categoryID IS NULL)")
    List<Product> searchProducts(@Param("keyword") String keyword, @Param("categoryID") Integer categoryID);
}

