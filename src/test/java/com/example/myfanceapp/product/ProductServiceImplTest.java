package com.example.myfanceapp.product;

import com.example.myfanceapp.interfaces.rest.form.ProductForm;
import com.example.myfanceapp.product.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  private final ProductRepository repository = Mockito.mock(ProductRepository.class);

  private final ProductServiceImpl service = new ProductServiceImpl(repository);

  @Test
  public void createProductShouldReturnNewlyCreatedProduct() {
    // given
    String productName = "test product";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    AsProduct newProduct = new ProductForm(productName, productPrice);

    Product expectedProduct = new Product(productName, productPrice);
    Mockito.when(repository.save(any(Product.class))).thenReturn(expectedProduct);

    // when
    Product createdProduct = service.createProduct(newProduct);

    // then
    Assertions.assertEquals(productName, createdProduct.getName());
    Assertions.assertEquals(productPrice, createdProduct.getPrice());
    Assertions.assertEquals(expectedProduct, createdProduct);
  }

  @Test
  public void getProductsShouldReturnsListOfProducts() {
    // given
    Product product1 = new Product("product 1 name", BigDecimal.valueOf(1));
    Product product2 = new Product("product 2 name", BigDecimal.valueOf(2));
    List<Product> expectedProducts = List.of(product1, product2);
    Mockito.when(repository.findAll()).thenReturn(expectedProducts);

    // when
    List<Product> products = service.getProducts();

    // then
    Assertions.assertEquals(2, products.size());
    Assertions.assertEquals(expectedProducts, products);
  }

  @Test
  public void getProductsShouldReturnsEmptyListOfProducts() {
    // given
    List<Product> expectedProducts = Collections.emptyList();
    Mockito.when(repository.findAll()).thenReturn(expectedProducts);

    // when
    List<Product> products = service.getProducts();

    // then
    Assertions.assertTrue(products.isEmpty());
  }

  @Test
  public void updateProductShouldReturnUpdatedProduct() {
    // given
    String productName = "test product";
    BigDecimal productPrice = BigDecimal.valueOf(1);
    AsProduct newProduct = new ProductForm(productName, productPrice);

    Product expectedProduct = new Product(productName, productPrice);
    Mockito.when(repository.save(any(Product.class))).thenReturn(expectedProduct);

    // when
    Product createdProduct = service.createProduct(newProduct);

    // then
    Assertions.assertEquals(productName, createdProduct.getName());
    Assertions.assertEquals(productPrice, createdProduct.getPrice());
    Assertions.assertEquals(expectedProduct, createdProduct);
  }

  @Test
  public void updateProductShouldThrowIllegalArgumentExceptionWhenNullProductIdProvided() {
    // given
    Integer nullableProductId = null;
    AsProduct newProduct = new ProductForm("name", BigDecimal.valueOf(1));

    String expectedExceptionMessage = "Product id is required";

    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.updateProduct(nullableProductId, newProduct));

    // then
    Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());
  }

  @Test
  public void updateProductShouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
    // given
    Integer notExistingProductId = 999;
    AsProduct newProduct = new ProductForm("name", BigDecimal.valueOf(1));

    String expectedExceptionMessage =
        String.format("Product with id:[%s] does not exist", notExistingProductId);

    // when
    Exception exception =
        assertThrows(
            ProductNotFoundException.class,
            () -> service.updateProduct(notExistingProductId, newProduct));

    // then
    Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());
  }
}
