package com.example.myfanceapp.interfaces.rest.form;

import com.example.myfanceapp.product.AsProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm implements AsProduct {

  @Schema(required = true, description = "Product name", example = "Product 1")
  private String name;

  @Schema(required = true, description = "Product price", example = "12.34")
  private BigDecimal price;
}
