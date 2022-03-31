package com.example.myfanceapp.interfaces.rest.form;

import com.example.myfanceapp.product.AsOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemForm implements AsOrderItem {

  @Schema(required = true, description = "Product id", example = "12")
  private Integer productId;

  @Schema(required = true, description = "Item quantity", example = "12")
  private Integer quantity;
}
