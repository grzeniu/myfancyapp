package com.example.myfanceapp.product.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class OrderItem implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private Integer quantity;

  // TODO I know that the precision for BigDecimal can causes problems
  // I would create own converter and store price as string
  @Column(precision = 5)
  private BigDecimal price;

  @ManyToOne
  @JoinColumn(name = "fk_product_id")
  private Product product;

  public OrderItem(Integer quantity, Product product) {
    this.quantity = quantity;
    this.product = product;
    this.price = product.getPrice().multiply(BigDecimal.valueOf(quantity));
  }
}
