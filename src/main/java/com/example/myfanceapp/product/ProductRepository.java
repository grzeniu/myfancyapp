package com.example.myfanceapp.product;

import com.example.myfanceapp.product.model.Product;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface ProductRepository extends Repository<Product, Integer> {

    List<Product> findAll();

    Product save(Product newProduct);

    Optional<Product> findById(Integer id);
}
