package com.example.myfanceapp.product.model;

import com.example.myfanceapp.product.AsProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // TODO name probably should be unique
    private String name;

    // TODO I know that the precision for BigDecimal can causes problems
    // I would create own converter and store price as string
    @Column(precision = 2)
    private BigDecimal price;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public void update(AsProduct updatedProduct) {
        this.name = updatedProduct.getName();
        this.price = updatedProduct.getPrice();
    }
}
