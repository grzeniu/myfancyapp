package com.example.myfanceapp.interfaces.rest;

import com.example.myfanceapp.interfaces.rest.form.ProductForm;
import com.example.myfanceapp.product.ProductService;
import com.example.myfanceapp.product.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product")
public class ProductController {

  private final ProductService service;

  // TODO add resource location header
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create new product", method = "POST")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<Product> createProduct(
      @Valid @RequestBody @Parameter(name = "productForm", description = "Product form")
          ProductForm productForm) {
    Product newProduct = service.createProduct(productForm);
    return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
  }

  @GetMapping
  @Operation(summary = "Get list of products", method = "GET")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<List<Product>> getProducts() {
    List<Product> products = service.getProducts();
    return ResponseEntity.ok(products);
  }

  @PutMapping("/{product_id}")
  @Operation(summary = "Update existing product", method = "GET")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Server failure")
      })
  public ResponseEntity<Product> updateProduct(
      @PathVariable("product_id") @Parameter(description = "Product id", example = "123")
          Integer productId,
      @Valid @RequestBody @Parameter(name = "productForm", description = "Product form")
          ProductForm product) {
    Product updatedProduct = service.updateProduct(productId, product);
    return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
  }
}
