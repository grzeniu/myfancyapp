package com.example.myfanceapp.product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface AsOrderItem {

  @NotNull(message = "Product id is required")
  Integer getProductId();

  @NotNull(message = "Item quantity is required")
  @Positive(message = "Item quantity must be greater than 0")
  Integer getQuantity();
}
