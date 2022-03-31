package com.example.myfanceapp.product;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.example.myfanceapp.interfaces.rest.form.OrderForm;
import com.example.myfanceapp.interfaces.rest.form.OrderItemForm;
import com.example.myfanceapp.product.model.OrderItem;
import com.example.myfanceapp.product.model.Product;
import com.example.myfanceapp.product.model.PurchaseOrder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  private final PurchaseOrderRepository orderRepository =
      Mockito.mock(PurchaseOrderRepository.class);
  private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
  private final ProductServiceImpl productService = new ProductServiceImpl(productRepository);

  private final OrderServiceImpl service = new OrderServiceImpl(orderRepository, productService);

  @Test
  public void placeOrderShouldReturnNewlyCreatedOrder() {
    // given
    String buyerEmail = "email@gmail.com";
    int itemQuantity = 1;
    BigDecimal productPrice = BigDecimal.valueOf(2);
    LocalDateTime createDate = LocalDateTime.now();

    List<AsOrderItem> asOrderItems = List.of(new OrderItemForm(1, itemQuantity));
    AsOrder orderParam = new OrderForm(buyerEmail, asOrderItems);

    Product product = new Product("product 1 name", productPrice);
    OrderItem orderItem = new OrderItem(itemQuantity, product);
    List<OrderItem> orderItems = List.of(orderItem);

    PurchaseOrder expectedOrder = new PurchaseOrder(orderItems, buyerEmail, createDate);
    Mockito.when(orderRepository.save(any(PurchaseOrder.class))).thenReturn(expectedOrder);

    Mockito.when(productRepository.findById(any(Integer.class))).thenReturn(Optional.of(product));

    // when
    PurchaseOrder order = service.placeOrder(orderParam);

    // then
    Assertions.assertEquals(buyerEmail, order.getBuyerEmail());
    Assertions.assertEquals(createDate, order.getCreateDateTime());
    Assertions.assertEquals(
        productPrice.multiply(BigDecimal.valueOf(itemQuantity)), order.getTotalPrice());
    Assertions.assertEquals(orderItems, order.getOrderItems());
    Assertions.assertEquals(
        productPrice.multiply(BigDecimal.valueOf(itemQuantity)),
        order.getOrderItems().get(0).getPrice());
    Assertions.assertEquals(itemQuantity, order.getOrderItems().get(0).getQuantity());
    Assertions.assertEquals(product, order.getOrderItems().get(0).getProduct());
  }

  @Test
  public void
      placeOrderShouldThrowProductNotFoundExceptionWhenAtLeastOneOfTheProductsDoesNotExist() {
    // given
    Integer notExistingProductId = 999;
    String buyerEmail = "email@gmail.com";
    int itemQuantity = 1;

    List<AsOrderItem> asOrderItems = List.of(new OrderItemForm(notExistingProductId, itemQuantity));
    AsOrder orderParam = new OrderForm(buyerEmail, asOrderItems);

    Mockito.when(productRepository.findById(any(Integer.class)))
        .thenReturn(Optional.ofNullable(null));

    String expectedExceptionMessage =
        String.format("Product with id:[%s] does not exist", notExistingProductId);

    // when
    Exception exception =
        assertThrows(ProductNotFoundException.class, () -> service.placeOrder(orderParam));

    // then
    Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());
  }

  @Test
  public void getOrdersShouldReturnsListOfProductsWithCreateDateBetweenProvidedTwoDates() {
    // given
    Product product1 = new Product("product 1 name", BigDecimal.valueOf(1));
    Product product2 = new Product("product 2 name", BigDecimal.valueOf(2));

    OrderItem orderItem1 = new OrderItem(1, product1);
    OrderItem orderItem2 = new OrderItem(2, product2);

    List<OrderItem> orderItems1 = List.of(orderItem1);
    List<OrderItem> orderItems2 = List.of(orderItem1, orderItem2);

    PurchaseOrder order1 =
        new PurchaseOrder(orderItems1, "email1@gmail.com", LocalDateTime.of(2022, 1, 1, 1, 1));
    PurchaseOrder order2 =
        new PurchaseOrder(orderItems2, "email1@gmail.com", LocalDateTime.of(2022, 1, 1, 1, 1));

    List<PurchaseOrder> expectedOrders = List.of(order1, order2);
    Mockito.when(
            orderRepository.findAllByCreateDateTimeBetween(
                any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(expectedOrders);

    LocalDateTime createDateFrom = LocalDateTime.of(2011, 1, 1, 1, 1);
    LocalDateTime createDateTo = LocalDateTime.of(2033, 1, 1, 1, 1);

    // when
    List<PurchaseOrder> orders = service.getOrders(createDateFrom, createDateTo);

    // then
    Assertions.assertEquals(2, orders.size());
    Assertions.assertEquals(expectedOrders, orders);
    Assertions.assertEquals(
        expectedOrders.get(0).getOrderItems().size(), orders.get(0).getOrderItems().size());
    Assertions.assertEquals(
        expectedOrders.get(1).getOrderItems().size(), orders.get(1).getOrderItems().size());
  }

  @Test
  public void getOrdersShouldReturnsEmptyListOfProducts() {
    // given
    List<PurchaseOrder> expectedOrders = Collections.emptyList();
    Mockito.when(
            orderRepository.findAllByCreateDateTimeBetween(
                any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(expectedOrders);

    LocalDateTime createDateFrom = LocalDateTime.of(2011, 1, 1, 1, 1);
    LocalDateTime createDateTo = LocalDateTime.of(2033, 1, 1, 1, 1);

    // when
    List<PurchaseOrder> orders = service.getOrders(createDateFrom, createDateTo);

    // then
    Assertions.assertTrue(orders.isEmpty());
  }
}
