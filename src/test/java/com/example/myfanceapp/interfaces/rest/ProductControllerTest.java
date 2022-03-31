package com.example.myfanceapp.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.myfanceapp.interfaces.rest.form.ProductForm;
import com.example.myfanceapp.product.AsProduct;
import com.example.myfanceapp.product.ProductNotFoundException;
import com.example.myfanceapp.product.ProductService;
import com.example.myfanceapp.product.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
@WebMvcTest(ProductController.class)
class ProductControllerTest {

  private final MockMvc mockMvc;

  private final ObjectMapper objectMapper;

  @MockBean private ProductService productService;

  @Autowired
  public ProductControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
  }

  @Test
  public void createProductShouldReturnNewlyCreatedProduct() throws Exception {
    // given
    String productName = "test product";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    ProductForm newProduct = new ProductForm(productName, productPrice);

    Product expectedProduct = new Product(productName, productPrice);
    Mockito.when(productService.createProduct(any(AsProduct.class))).thenReturn(expectedProduct);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", Matchers.equalTo(productName)))
        .andExpect(jsonPath("$.price", Matchers.equalTo(productPrice.intValue())));
  }

  @Test
  public void createProductShouldReturnStatus400WhenRequestIsBad() throws Exception {
    // given
    String emptyProductName = "";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    ProductForm newProduct = new ProductForm(emptyProductName, productPrice);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field", Matchers.equalTo("name")))
        .andExpect(
            jsonPath("$.errors[0].descriptions", Matchers.equalTo("Product name cannot be empty")));
  }

  @Test
  public void createProductShouldReturnStatus500WhenUnexpectedExceptionOccurred() throws Exception {
    // given
    String productName = "test product";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    ProductForm newProduct = new ProductForm(productName, productPrice);

    Mockito.when(productService.getProducts()).thenThrow(RuntimeException.class);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isCreated());
  }

  @Test
  public void getProductsShouldReturnListOfProducts() throws Exception {
    // given
    String product1Name = "product 1 name";
    BigDecimal product1Price = BigDecimal.valueOf(1);
    Product product1 = new Product(product1Name, product1Price);
    Product product2 = new Product("product 2 name", BigDecimal.valueOf(2));
    List<Product> expectedProducts = List.of(product1, product2);
    Mockito.when(productService.getProducts()).thenReturn(expectedProducts);

    // expected
    mockMvc
        .perform(MockMvcRequestBuilders.get("/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", Matchers.is(2)))
        .andExpect(jsonPath("$[0].name", Matchers.equalTo(product1Name)))
        .andExpect(jsonPath("$[0].price", Matchers.equalTo(product1Price.intValue())));
  }

  @Test
  public void getProductsShouldReturnEmptyListOfProducts() throws Exception {
    // given
    Mockito.when(productService.getProducts()).thenReturn(Collections.emptyList());

    // expected
    mockMvc
        .perform(MockMvcRequestBuilders.get("/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", Matchers.is(0)));
  }

  @Test
  public void getProductsShouldReturnStatus500WhenUnexpectedExceptionOccurred() throws Exception {
    // given
    Mockito.when(productService.getProducts()).thenThrow(RuntimeException.class);

    // expected
    mockMvc
        .perform(MockMvcRequestBuilders.get("/products"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors.size()", Matchers.is(1)));
  }

  @Test
  public void updateProductShouldReturnUpdatedProduct() throws Exception {
    // given
    String productName = "test product";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    ProductForm newProduct = new ProductForm(productName, productPrice);

    Product expectedProduct = new Product(productName, productPrice);
    Mockito.when(productService.updateProduct(any(Integer.class), any(AsProduct.class)))
        .thenReturn(expectedProduct);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", Matchers.equalTo(productName)))
        .andExpect(jsonPath("$.price", Matchers.equalTo(productPrice.intValue())));
  }

  @Test
  public void updateProductShouldReturnStatus404WhenProductNotExist() throws Exception {
    // given
    String productName = "test product";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    ProductForm newProduct = new ProductForm(productName, productPrice);

    Mockito.when(productService.updateProduct(any(Integer.class), any(AsProduct.class)))
        .thenThrow(ProductNotFoundException.class);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateProductShouldReturnStatus404WhenRequestIsBad() throws Exception {
    // given
    String incorrectProductName = "";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    ProductForm newProduct = new ProductForm(incorrectProductName, productPrice);

    Mockito.when(productService.updateProduct(any(Integer.class), any(AsProduct.class)))
        .thenThrow(ProductNotFoundException.class);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field", Matchers.equalTo("name")))
        .andExpect(
            jsonPath("$.errors[0].descriptions", Matchers.equalTo("Product name cannot be empty")));
  }

  @Test
  public void updateProductShouldReturn500WhenUnexpectedExceptionOccurred() throws Exception {
    // given
    String productName = "product name";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    ProductForm newProduct = new ProductForm(productName, productPrice);

    Mockito.when(productService.updateProduct(any(Integer.class), any(AsProduct.class)))
        .thenThrow(RuntimeException.class);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newProduct)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors.size()", Matchers.is(1)));
  }
}
