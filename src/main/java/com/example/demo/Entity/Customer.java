package com.example.demo.Entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
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
@Table (name = "customer")
public class Customer {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerID;

   
    private String customername;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

   
    private String email;

  
    private String phone;
    private  String address;
   
    private Boolean gender;
    private Boolean verified; 

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "accountID")
    private Account account;

    @JsonIgnore
    @OneToOne(mappedBy = "customer")  // Mối quan hệ một-nhiều với GioHang
    private Cart cart;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;


    
}