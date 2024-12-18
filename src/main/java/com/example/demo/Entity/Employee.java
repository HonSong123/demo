 package com.example.demo.Entity;

 import java.io.Serializable;
 import java.util.Date;
 import java.util.List;

 import com.fasterxml.jackson.annotation.JsonIgnore;

 import jakarta.persistence.Entity;
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
 @NoArgsConstructor
 @AllArgsConstructor
 @Table(name = "employee")
 public class Employee implements Serializable {
 	@Id
 	private Integer employeeid;

 	private String employeename;
	
 	private Date birthdate;
	
 	private String email;
	
 	private String phone;
	
 	private boolean gender;
	

	
 	@ManyToOne
     @JoinColumn(name = "accountid")
     private Account account;
	
 	@JsonIgnore
 	 @OneToMany(mappedBy = "employee")
 	 private List<Order> orders;
	
 	 @JsonIgnore
      @OneToMany(mappedBy = "employee")
      private List<WarehouseReceipt> warehouseReceipts;
 }
