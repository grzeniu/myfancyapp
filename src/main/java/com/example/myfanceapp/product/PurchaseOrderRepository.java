package com.example.myfanceapp.product;

import com.example.myfanceapp.product.model.PurchaseOrder;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

interface PurchaseOrderRepository extends Repository<PurchaseOrder, Integer> {

  PurchaseOrder save(PurchaseOrder order);

  @EntityGraph(
      attributePaths = {"orderItems", "orderItems.product"},
      type = EntityGraph.EntityGraphType.FETCH)
  List<PurchaseOrder> findAllByCreateDateTimeBetween(
      LocalDateTime creationDateFrom, LocalDateTime creationDateTo);
}
