package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supplier")
public class Supplier implements Serializable {
    @Id
    private String supplierid;

    private String suppliername;

    private String email;

    private String phone;

    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "supplier")
    private List<WarehouseReceipt> warehouseReceipts;
}
