package com.example.myfanceapp.product;

import java.math.BigDecimal;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public interface AsProduct {

  @NotEmpty(message = "Product name cannot be empty")
  @Size(max = 255, message = "Product name cannot be longer than 255 characters")
  String getName();

  @NotNull(message = "Price is required")
  @Positive(message = "Price must be greater than 0")
  BigDecimal getPrice();
}
