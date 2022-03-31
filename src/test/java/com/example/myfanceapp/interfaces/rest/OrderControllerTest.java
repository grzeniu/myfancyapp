package com.example.myfanceapp.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.myfanceapp.interfaces.rest.form.OrderForm;
import com.example.myfanceapp.interfaces.rest.form.OrderItemForm;
import com.example.myfanceapp.product.AsOrder;
import com.example.myfanceapp.product.AsOrderItem;
import com.example.myfanceapp.product.OrderService;
import com.example.myfanceapp.product.model.OrderItem;
import com.example.myfanceapp.product.model.Product;
import com.example.myfanceapp.product.model.PurchaseOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// TODO debatable whether these are unit tests :)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

  private final MockMvc mockMvc;

  private final ObjectMapper objectMapper;

  @MockBean private OrderService orderService;

  @Autowired
  public OrderControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
  }

  @Test
  public void placeOrderShouldReturnNewlyCreatedProduct() throws Exception {
    // given
    String buyerEmail = "email@gmail.com";
    int itemQuantity = 1;
    BigDecimal price = BigDecimal.valueOf(2);
    String productName = "product 1 name";

    List<AsOrderItem> asOrderItems = List.of(new OrderItemForm(1, itemQuantity));
    AsOrder order = new OrderForm(buyerEmail, asOrderItems);

    Product product = new Product(productName, price);
    OrderItem orderItem = new OrderItem(itemQuantity, product);
    List<OrderItem> orderItems = List.of(orderItem);

    PurchaseOrder expectedOrder =
        new PurchaseOrder(orderItems, buyerEmail, LocalDateTime.of(2022, 1, 1, 1, 1));
    Mockito.when(orderService.placeOrder(any(AsOrder.class))).thenReturn(expectedOrder);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(order)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.buyerEmail", Matchers.equalTo(buyerEmail)))
        .andExpect(jsonPath("$.createDateTime", Matchers.equalTo("2022-01-01T01:01:00")))
        .andExpect(jsonPath("$.totalPrice", Matchers.equalTo(price.intValue())))
        .andExpect(jsonPath("$.orderItems[0].quantity", Matchers.equalTo(itemQuantity)))
        .andExpect(jsonPath("$.orderItems[0].price", Matchers.equalTo(price.intValue())))
        .andExpect(jsonPath("$.orderItems[0].product.name", Matchers.equalTo(productName)))
        .andExpect(jsonPath("$.orderItems[0].product.price", Matchers.equalTo(price.intValue())));
  }

  @Test
  public void placeOrderShouldReturnStatus400WhenRequestIsBad() throws Exception {
    // given
    AsOrder order = new OrderForm("email@gmail.com", Collections.emptyList());

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(order)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field", Matchers.equalTo("orderItems")))
        .andExpect(
            jsonPath(
                "$.errors[0].descriptions",
                Matchers.equalTo("List of order items cannot be empty")));
  }

  @Test
  public void placeOrderShouldReturnStatus500WhenUnexpectedExceptionOccurred() throws Exception {
    // given
    String buyerEmail = "email@gmail.com";
    int itemQuantity = 1;

    List<AsOrderItem> asOrderItems = List.of(new OrderItemForm(1, itemQuantity));
    AsOrder order = new OrderForm(buyerEmail, asOrderItems);

    Mockito.when(orderService.placeOrder(any(AsOrder.class))).thenThrow(RuntimeException.class);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(order)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors.size()", Matchers.is(1)));
  }

  @Test
  public void getOrdersShouldReturnListOfProducts() throws Exception {
    // given
    String buyerEmail = "email1@gmail.com";
    String productName = "product 1 name";
    BigDecimal price = BigDecimal.valueOf(1);
    Product product = new Product(productName, price);
    OrderItem orderItem = new OrderItem(1, product);
    List<OrderItem> orderItems = List.of(orderItem);
    PurchaseOrder order =
        new PurchaseOrder(orderItems, buyerEmail, LocalDateTime.of(2022, 1, 1, 1, 1));
    List<PurchaseOrder> expectedOrders = List.of(order);
    Mockito.when(orderService.getOrders(any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(expectedOrders);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/orders")
                .param("date_from", "2000-10-10T10:10:10")
                .param("date_to", "2020-10-10T10:10:10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].buyerEmail", Matchers.equalTo(buyerEmail)))
        .andExpect(jsonPath("$[0].createDateTime", Matchers.equalTo("2022-01-01T01:01:00")))
        .andExpect(jsonPath("$[0].totalPrice", Matchers.equalTo(price.intValue())))
        .andExpect(jsonPath("$[0].orderItems[0].quantity", Matchers.equalTo(1)))
        .andExpect(jsonPath("$[0].orderItems[0].price", Matchers.equalTo(price.intValue())))
        .andExpect(jsonPath("$[0].orderItems[0].product.name", Matchers.equalTo(productName)))
        .andExpect(
            jsonPath("$[0].orderItems[0].product.price", Matchers.equalTo(price.intValue())));
  }

  @Test
  public void getOrdersShouldReturnEmptyListOfProducts() throws Exception {
    // given
    Mockito.when(orderService.getOrders(any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(Collections.emptyList());

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/orders")
                .param("date_from", "2000-10-10T10:10:10")
                .param("date_to", "2020-10-10T10:10:10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", Matchers.is(0)));
  }

  @Test
  public void getOrdersShouldReturnStatus500WhenUnexpectedExceptionOccurred() throws Exception {
    // given
    Mockito.when(orderService.getOrders(any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenThrow(RuntimeException.class);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/orders")
                .param("date_from", "2000-10-10T10:10:10")
                .param("date_to", "2020-10-10T10:10:10"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors.size()", Matchers.is(1)));
  }
}
