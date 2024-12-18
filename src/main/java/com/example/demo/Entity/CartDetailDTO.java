package com.example.demo.Entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDTO {
    private String productName;
    private Integer productID;  // Tên sản phẩm
    private float price; // Giá sản phẩm
    private String image; // Hình ảnh sản phẩm
    private int quantity; // Số lượng sản phẩm
}
 

