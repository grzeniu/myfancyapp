package com.example.myfanceapp.interfaces.rest.form;

import com.example.myfanceapp.product.AsProduct;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm implements AsProduct {

  private String name;

  private BigDecimal price;
}
