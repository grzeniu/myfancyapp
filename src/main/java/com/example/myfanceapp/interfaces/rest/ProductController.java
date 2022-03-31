package com.example.myfanceapp.interfaces.rest;

import com.example.myfanceapp.interfaces.rest.form.ProductForm;
import com.example.myfanceapp.product.ProductService;
import com.example.myfanceapp.product.model.Product;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody
                    ProductForm productForm) {
        Product newProduct = service.createProduct(productForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = service.getProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("product_id")
                    Integer productId,
            @Valid @RequestBody
                    ProductForm product) {
        Product updatedProduct = service.updateProduct(productId, product);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
}
