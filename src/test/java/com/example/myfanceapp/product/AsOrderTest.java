package com.example.myfanceapp.product;

import com.example.myfanceapp.interfaces.rest.form.OrderForm;
import com.example.myfanceapp.interfaces.rest.form.OrderItemForm;
import java.util.Collections;
import java.util.List;
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

public class AsOrderTest {

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
    AsOrder order = new OrderForm("email@gmail.com", List.of(orderItem));

    // when
    Set<ConstraintViolation<AsOrder>> violations = validator.validate(order);

    // then
    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldDetectInvalidEmail() {
    // given
    AsOrderItem orderItem = new OrderItemForm(1, 1);
    AsOrder order = new OrderForm("invalid_email", List.of(orderItem));

    // when
    Set<ConstraintViolation<AsOrder>> violations = validator.validate(order);

    // then
    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals(
        "Buyer's email has incorrect format",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectNullAsEmail() {
    // given
    AsOrderItem orderItem = new OrderItemForm(1, 1);
    AsOrder order = new OrderForm(null, List.of(orderItem));

    // when
    Set<ConstraintViolation<AsOrder>> violations = validator.validate(order);

    // then
    Assertions.assertEquals(
        "Buyer's email is required",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectEmptyOrderItemsList() {
    // given
    AsOrder order = new OrderForm("email@gmail.com", Collections.emptyList());

    // when
    Set<ConstraintViolation<AsOrder>> violations = validator.validate(order);

    // then
    Assertions.assertEquals(
        "List of order items cannot be empty",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectThatMoreThanOneFieldIsNotValid() {
    // given
    AsOrder order = new OrderForm(null, Collections.emptyList());

    // when
    Set<ConstraintViolation<AsOrder>> violations = validator.validate(order);

    // then
    Assertions.assertEquals(2, violations.toArray().length);
  }
}
