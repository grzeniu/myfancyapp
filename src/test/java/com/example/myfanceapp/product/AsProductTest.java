package com.example.myfanceapp.product;

import com.example.myfanceapp.interfaces.rest.form.ProductForm;
import java.math.BigDecimal;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import net.bytebuddy.utility.RandomString;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AsProductTest {

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
    AsProduct product = new ProductForm("name", BigDecimal.valueOf(1));

    // when
    Set<ConstraintViolation<AsProduct>> violations = validator.validate(product);

    // then
    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldDetectEmptyName() {
    // given
    AsProduct product = new ProductForm("", BigDecimal.valueOf(1));

    // when
    Set<ConstraintViolation<AsProduct>> violations = validator.validate(product);

    // then
    Assertions.assertEquals(
        "Product name cannot be empty",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectTooLongName() {
    // given
    RandomString gen = new RandomString(300);
    AsProduct product = new ProductForm(gen.nextString(), BigDecimal.valueOf(1));

    // when
    Set<ConstraintViolation<AsProduct>> violations = validator.validate(product);

    // then
    Assertions.assertEquals(
        "Product name cannot be longer than 255 characters",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectNullAsPrice() {
    // given
    AsProduct product = new ProductForm("name", null);

    // when
    Set<ConstraintViolation<AsProduct>> violations = validator.validate(product);

    // then
    Assertions.assertEquals(
        "Price is required", ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectNonPositivePrice() {
    // given
    AsProduct product = new ProductForm("name", BigDecimal.valueOf(-1));

    // when
    Set<ConstraintViolation<AsProduct>> violations = validator.validate(product);

    // then
    Assertions.assertEquals(
        "Price must be greater than 0",
        ((ConstraintViolationImpl) violations.toArray()[0]).getMessage());
  }

  @Test
  public void shouldDetectThatMoreThanOneFieldIsNotValid() {
    // given
    AsProduct product = new ProductForm("", BigDecimal.valueOf(-1));

    // when
    Set<ConstraintViolation<AsProduct>> violations = validator.validate(product);

    // then
    Assertions.assertEquals(2, violations.toArray().length);
  }
}
