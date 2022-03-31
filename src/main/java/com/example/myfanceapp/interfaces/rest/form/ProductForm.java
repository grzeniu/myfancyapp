package com.example.myfanceapp.interfaces.rest.form;

import com.example.myfanceapp.product.AsProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm implements AsProduct {

    private String name;

    private BigDecimal price;
}
