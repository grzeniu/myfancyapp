package com.example.myfanceapp.product;

import com.example.myfanceapp.product.model.PurchaseOrder;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
  PurchaseOrder placeOrder(AsOrder order);

  List<PurchaseOrder> getOrders(LocalDateTime creationDateFrom, LocalDateTime creationDateTo);
}
