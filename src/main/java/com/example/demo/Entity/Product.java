package com.example.demo.Entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table ( name = "product")
public class Product implements Serializable{
    @Id
    @Column(name = "productID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;

    private String productname;
    private String origin;
    private String unit;
    private float price;

    private String description;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Category category;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productimage;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
	List<CartDetail> cartDetails;
    // Getters and setters
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<WarehouseReceiptDetail> warehouseReceiptDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Promotion> promotions;

    @JsonIgnore
	 @OneToMany(mappedBy = "product")
	 private List<ProductReviews> productReviews;
    
}