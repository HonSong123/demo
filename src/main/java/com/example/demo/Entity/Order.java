	package com.example.demo.Entity;

	import java.util.ArrayList;
	import java.util.Date;
	import java.util.List;

	import com.fasterxml.jackson.annotation.JsonIgnore;

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
	import jakarta.persistence.Temporal;
	import jakarta.persistence.TemporalType;
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;

	@Data
	@Entity
	@NoArgsConstructor
	@AllArgsConstructor
	@Table(name = "orders")
	public class Order {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer orderid;

		private String paymentmethod;
		
		private String paymentstatus;
		
		private String deliverystatus;
		
		@Temporal(TemporalType.DATE)
		@Column(name = "Createdate")
		Date createDate = new Date();
		
		private Double totalamount;

		@ManyToOne
		@JoinColumn(name = "cartid") 
		private Cart cart; 
		
		@ManyToOne
		@JoinColumn(name = "customerid") 
		private Customer customer; 
		
		 @ManyToOne
		 @JoinColumn(name = "employeeid")
		 private Employee employee;
		
		
		@JsonIgnore
		 @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

	

	}
