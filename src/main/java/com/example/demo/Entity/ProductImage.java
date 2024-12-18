package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table ( name = "productimage")
public class ProductImage {
    @Id
    private String imageID;

    private String image;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    // Getters and setters
}
