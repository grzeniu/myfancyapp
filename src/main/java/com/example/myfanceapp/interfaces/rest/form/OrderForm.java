package com.example.myfanceapp.interfaces.rest.form;

import com.example.myfanceapp.product.AsOrder;
import com.example.myfanceapp.product.AsOrderItem;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm implements AsOrder {

  @Schema(required = true, description = "Buyer's email", example = "example@email.com")
  private String buyerEmail;

  @Schema(required = true, description = "List of order items")
  @JsonDeserialize(contentAs = OrderItemForm.class)
  private List<AsOrderItem> orderItems;
}
