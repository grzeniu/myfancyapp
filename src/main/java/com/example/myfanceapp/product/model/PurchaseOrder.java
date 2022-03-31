package com.example.myfanceapp.product.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO I could not use Order as table due to db restrictions
@Entity
@NoArgsConstructor
@Getter
@NamedEntityGraph(
    name = "PurchaseOrder.orderItems",
    attributeNodes = @NamedAttributeNode(value = "orderItems", subgraph = "OrderItems.product"),
    subclassSubgraphs = {
      @NamedSubgraph(
          name = "OrderItems.product",
          attributeNodes = @NamedAttributeNode(value = "product"))
    })
public class PurchaseOrder implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @OneToMany(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "fk_purchase_order_id")
  private List<OrderItem> orderItems = new LinkedList<>();

  private String buyerEmail;

  private LocalDateTime createDateTime;

  @Transient private BigDecimal totalPrice;

  public PurchaseOrder(
      List<OrderItem> orderItems, String buyerEmail, LocalDateTime createDateTime) {
    this.orderItems = orderItems;
    this.buyerEmail = buyerEmail;
    this.createDateTime = createDateTime;
    this.totalPrice = calcTotalPrice();
  }

  @PostLoad
  protected void initTotalPrice() {
    this.totalPrice = calcTotalPrice();
  }

  private BigDecimal calcTotalPrice() {
    return orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
