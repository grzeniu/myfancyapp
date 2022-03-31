package com.example.myfanceapp.product;

import com.example.myfanceapp.product.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    Product createProduct(AsProduct newProduct);

    Product updateProduct(Integer productId, AsProduct product);

    Product getProduct(Integer productId);
}
