package com.example.myfanceapp.product;

import com.example.myfanceapp.product.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

interface ProductRepository extends Repository<Product, Integer> {

  List<Product> findAll();

  Product save(Product newProduct);

  Optional<Product> findById(Integer id);
}
