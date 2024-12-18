package com.example.demo.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCount {
    private String categoryID;
    private String categoryName;
    private long productCount;

}
