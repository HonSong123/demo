package com.example.demo.Entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "previewss")
public class ProductReviews implements Serializable {
	@Id
	private Integer reviewid;
	
	private Integer rating;
	
	@ManyToOne
    @JoinColumn(name = "productid") 
    private Product product; 
	
	@ManyToOne
    @JoinColumn(name = "customerid") 
    private Customer customer;
	
	private String reviewtext;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "Reviewdate")
	Date reviewDate = new Date();
	
}
