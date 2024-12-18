package com.example.demo.Entity;

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
@Table (name = "account")
public class Account {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    @Column(nullable = true, length = 50)
    private String username;

    @Column(nullable = true, length = 50)
    private String password;

    @Column(nullable = true)
    private Integer role;

    @Column(nullable = true)
    private Boolean isverified;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Customer customer;

    @Column(nullable = true)
    private Boolean isgoogleaccount;


     @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Employee> employees;
}