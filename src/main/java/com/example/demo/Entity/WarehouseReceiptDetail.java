package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehousereceiptdetail")
public class WarehouseReceiptDetail implements Serializable {
    @Id
    private Integer detailid;

    @ManyToOne
    @JoinColumn(name = "receiptid")
    private WarehouseReceipt warehouseReceipt;


    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    private Integer quantity;

    private Double unitprice;


}
