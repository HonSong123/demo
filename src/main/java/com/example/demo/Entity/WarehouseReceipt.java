package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehousereceipt")
public class WarehouseReceipt implements Serializable {
    @Id
    private Integer receiptid;

    @Temporal(TemporalType.DATE)
    @Column(name = "Receiptdate")
    Date receiptDate = new Date();

    private Double totalamount;

    private boolean paymentstatus;

    @ManyToOne
    @JoinColumn(name = "supplierid")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "employeeid")
    private Employee employee;

    @OneToMany(mappedBy = "warehouseReceipt")
    @JsonIgnore
    private List<WarehouseReceiptDetail> warehouseReceiptDetails;
}
