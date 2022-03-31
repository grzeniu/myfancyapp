package com.example.myfanceapp.product;

import com.example.myfanceapp.product.model.OrderItem;
import com.example.myfanceapp.product.model.Product;
import com.example.myfanceapp.product.model.PurchaseOrder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class OrderServiceImpl implements OrderService {

  private final PurchaseOrderRepository repository;
  private final ProductService productService;

  @Override
  @Transactional
  public PurchaseOrder placeOrder(AsOrder newOrder) {
    PurchaseOrder order = buildPurchaseOrder(newOrder);
    return repository.save(order);
  }

  @Override
  public List<PurchaseOrder> getOrders(
      LocalDateTime creationDateFrom, LocalDateTime creationDateTo) {
    return repository.findAllByCreateDateTimeBetween(creationDateFrom, creationDateTo);
  }

  private PurchaseOrder buildPurchaseOrder(AsOrder newOrder) {
    List<OrderItem> items = buildOrderItems(newOrder.getOrderItems());
    return new PurchaseOrder(items, newOrder.getBuyerEmail(), LocalDateTime.now());
  }

  private List<OrderItem> buildOrderItems(List<AsOrderItem> orderItems) {
    return orderItems.stream()
        .map(
            item -> {
              Product product = productService.getProduct(item.getProductId());
              return new OrderItem(item.getQuantity(), product);
            })
        .collect(Collectors.toList());
  }
}
