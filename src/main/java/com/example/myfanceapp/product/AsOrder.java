package com.example.myfanceapp.product;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface AsOrder {

  @Email(regexp = "^(.+)@(.+)$", message = "Buyer's email has incorrect format")
  @NotNull(message = "Buyer's email is required")
  String getBuyerEmail();

  @Valid
  @NotEmpty(message = "List of order items cannot be empty")
  List<AsOrderItem> getOrderItems();
}
