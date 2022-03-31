package com.example.myfanceapp.product;

import com.example.myfanceapp.interfaces.rest.form.OrderItemForm;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AsOrderItemTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeAll
  public static void createValidator() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  public static void close() {
    validatorFactory.close();
  }

  @Test
  public void shouldHaveNoViolations() {
    // given
    AsOrderItem orderItem = new OrderItemForm(1, 1);

    // when
    Set<ConstraintViolation<AsOrderItem>> violations = validator.validate(orderItem);

    // then
    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldDetectNonPositiveQuantity() {
    // given
    AsOrderItem orderItem = new OrderItemForm(1, -1);

    // when
    Set<ConstraintViolation<AsOrderItem>> violations = validator.validate(orderItem);

    // then
    Assertions.assertEquals(
        "Item quantity must be greater than 0",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectNullAsQuantity() {
    // given
    AsOrderItem orderItem = new OrderItemForm(1, null);

    // when
    Set<ConstraintViolation<AsOrderItem>> violations = validator.validate(orderItem);

    // then
    Assertions.assertEquals(
        "Item quantity is required",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectNullAsProductId() {
    // given
    AsOrderItem orderItem = new OrderItemForm(null, 1);

    // when
    Set<ConstraintViolation<AsOrderItem>> violations = validator.validate(orderItem);

    // then
    Assertions.assertEquals(
        "Product id is required", ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectThatMoreThanOneFieldIsNotValid() {
    // given
    AsOrderItem orderItem = new OrderItemForm(null, null);

    // when
    Set<ConstraintViolation<AsOrderItem>> violations = validator.validate(orderItem);

    // then
    Assertions.assertEquals(2, violations.toArray().length);
  }
}
