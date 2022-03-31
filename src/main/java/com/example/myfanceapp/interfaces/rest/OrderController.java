package com.example.myfanceapp.interfaces.rest;

import com.example.myfanceapp.interfaces.rest.form.OrderForm;
import com.example.myfanceapp.product.OrderService;
import com.example.myfanceapp.product.model.PurchaseOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order")
public class OrderController {

  private final OrderService service;

  // TODO add resource location header
  @PostMapping
  @Operation(summary = "Place new order", method = "POST")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<PurchaseOrder> placeOrder(
      @Valid @RequestBody @Parameter(name = "OrderForm", description = "Order Form")
          OrderForm orderForm) {
    PurchaseOrder newOrder = service.placeOrder(orderForm);
    return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
  }

  @GetMapping
  @Operation(summary = "Get list of orders", method = "POST")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<List<PurchaseOrder>> getOrders(
      @RequestParam(value = "date_from")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          @Parameter(description = "Creation date from", example = "2000-10-10T10:10:10")
          LocalDateTime creationDateFrom,
      @RequestParam(value = "date_to")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          @Parameter(description = "Creation date to", example = "2345-10-10T10:10:10")
          LocalDateTime creationDateTo) {
    List<PurchaseOrder> orders = service.getOrders(creationDateFrom, creationDateTo);
    return ResponseEntity.ok(orders);
  }
}
