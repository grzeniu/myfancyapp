package com.example.myfanceapp.product;

import com.example.myfanceapp.product.model.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {

  private final ProductRepository repository;

  // TODO I would validate if product already exists but I do not which field should be unique
  @Override
  public Product createProduct(AsProduct newProduct) {
    Product product = buildProduct(newProduct);
    return repository.save(product);
  }

  @Override
  public List<Product> getProducts() {
    return repository.findAll();
  }

  @Override
  @Transactional
  public Product updateProduct(Integer productId, AsProduct updatedProduct) {
    if (productId == null) {
      throw new IllegalArgumentException("Product id is required");
    }

    Product productToUpdate = getProductById(productId);
    productToUpdate.update(updatedProduct);
    return repository.save(productToUpdate);
  }

  @Override
  public Product getProduct(Integer productId) {
    return getProductById(productId);
  }

  // TODO for bigger class I would create factory
  private Product buildProduct(AsProduct newProduct) {
    return new Product(newProduct.getName(), newProduct.getPrice());
  }

  private Product getProductById(Integer productId) {
    return repository
        .findById(productId)
        .orElseThrow(
            () ->
                new ProductNotFoundException(
                    String.format("Product with id:[%s] does not exist", productId)));
  }
}
