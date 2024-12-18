package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "cartdetail")
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailID;

    @ManyToOne
    @JoinColumn(name = "cartID")
    @JsonIgnore
    private Cart cart;
    
    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID", nullable = false)
    private Product product;

    public int getProductId() {
        return product != null ? product.getProductID() : null; // Ensure the product exists before calling getProductId()
    }
    
    @Transient // Optional: if you don't want to persist this field in the database
    private boolean selected;

    private Integer quantity;

    // Getters và Setters
    public CartDetail(Integer detailID, Cart cart, Product product, int quantity) {
        this.detailID = detailID;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    
}
