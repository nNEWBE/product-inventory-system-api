package com.example.product_inventory_system_api.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCalculatorTest {

    private ProductCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ProductCalculator();
    }

    @Nested
    @DisplayName("calculateDiscountedPrice Tests")
    class CalculateDiscountedPriceTests {

        @Test
        @DisplayName("Should return original price when discount rate is 0%")
        void shouldReturnOriginalPriceWhenNoDiscount() {
            double originalPrice = 100.0;
            double discountRate = 0.0;

            double result = calculator.calculateDiscountedPrice(originalPrice, discountRate);

            assertEquals(100.0, result, 0.001, "Price should remain unchanged with 0% discount");
        }

        @Test
        @DisplayName("Should return half price when discount rate is 50%")
        void shouldReturnHalfPriceWithFiftyPercentDiscount() {
            double originalPrice = 200.0;
            double discountRate = 50.0;

            double result = calculator.calculateDiscountedPrice(originalPrice, discountRate);

            assertEquals(100.0, result, 0.001, "Price should be halved with 50% discount");
        }

        @Test
        @DisplayName("Should return zero when discount rate is 100%")
        void shouldReturnZeroWithFullDiscount() {
            double originalPrice = 150.0;
            double discountRate = 100.0;

            double result = calculator.calculateDiscountedPrice(originalPrice, discountRate);

            assertEquals(0.0, result, 0.001, "Price should be zero with 100% discount");
        }

        @Test
        @DisplayName("Should correctly calculate partial discount")
        void shouldCorrectlyCalculatePartialDiscount() {
            double originalPrice = 80.0;
            double discountRate = 25.0;

            double result = calculator.calculateDiscountedPrice(originalPrice, discountRate);

            assertEquals(60.0, result, 0.001, "25% off $80 should be $60");
        }

        @Test
        @DisplayName("Should handle small discount rates")
        void shouldHandleSmallDiscountRate() {
            double originalPrice = 1000.0;
            double discountRate = 1.0;

            double result = calculator.calculateDiscountedPrice(originalPrice, discountRate);

            assertEquals(990.0, result, 0.001, "1% off $1000 should be $990");
        }

        @Test
        @DisplayName("Should throw exception for negative original price")
        void shouldThrowExceptionForNegativePrice() {
            double originalPrice = -50.0;
            double discountRate = 10.0;

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.calculateDiscountedPrice(originalPrice, discountRate));
            assertEquals("Original price cannot be negative", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for negative discount rate")
        void shouldThrowExceptionForNegativeDiscountRate() {
            double originalPrice = 100.0;
            double discountRate = -10.0;

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.calculateDiscountedPrice(originalPrice, discountRate));
            assertEquals("Discount rate must be between 0 and 100", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for discount rate over 100%")
        void shouldThrowExceptionForDiscountRateOverHundred() {
            double originalPrice = 100.0;
            double discountRate = 150.0;

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.calculateDiscountedPrice(originalPrice, discountRate));
            assertEquals("Discount rate must be between 0 and 100", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle zero original price")
        void shouldHandleZeroOriginalPrice() {
            double originalPrice = 0.0;
            double discountRate = 50.0;

            double result = calculator.calculateDiscountedPrice(originalPrice, discountRate);

            assertEquals(0.0, result, 0.001, "Zero price should remain zero");
        }
    }

    @Nested
    @DisplayName("isQuantitySufficient Tests")
    class IsQuantitySufficientTests {

        @Test
        @DisplayName("Should return true when current quantity exceeds required")
        void shouldReturnTrueWhenQuantityExceedsRequired() {
            int currentQuantity = 100;
            int requiredQuantity = 50;

            boolean result = calculator.isQuantitySufficient(currentQuantity, requiredQuantity);

            assertTrue(result, "Should be sufficient when stock exceeds requirement");
        }

        @Test
        @DisplayName("Should return true when current quantity equals required")
        void shouldReturnTrueWhenQuantityEqualsRequired() {
            int currentQuantity = 50;
            int requiredQuantity = 50;

            boolean result = calculator.isQuantitySufficient(currentQuantity, requiredQuantity);

            assertTrue(result, "Should be sufficient when stock exactly matches requirement");
        }

        @Test
        @DisplayName("Should return false when current quantity is less than required")
        void shouldReturnFalseWhenQuantityInsufficient() {
            int currentQuantity = 30;
            int requiredQuantity = 50;

            boolean result = calculator.isQuantitySufficient(currentQuantity, requiredQuantity);

            assertFalse(result, "Should be insufficient when stock is below requirement");
        }

        @Test
        @DisplayName("Should return true when required quantity is zero")
        void shouldReturnTrueWhenRequiredIsZero() {
            int currentQuantity = 10;
            int requiredQuantity = 0;

            boolean result = calculator.isQuantitySufficient(currentQuantity, requiredQuantity);

            assertTrue(result, "Any stock should be sufficient for zero requirement");
        }

        @Test
        @DisplayName("Should return true when both quantities are zero")
        void shouldReturnTrueWhenBothAreZero() {
            int currentQuantity = 0;
            int requiredQuantity = 0;

            boolean result = calculator.isQuantitySufficient(currentQuantity, requiredQuantity);

            assertTrue(result, "Zero stock should be sufficient for zero requirement");
        }

        @Test
        @DisplayName("Should return false when current is zero but required is positive")
        void shouldReturnFalseWhenNoStockButRequired() {
            int currentQuantity = 0;
            int requiredQuantity = 5;

            boolean result = calculator.isQuantitySufficient(currentQuantity, requiredQuantity);

            assertFalse(result, "Zero stock should be insufficient for any positive requirement");
        }

        @Test
        @DisplayName("Should throw exception for negative current quantity")
        void shouldThrowExceptionForNegativeCurrentQuantity() {
            int currentQuantity = -10;
            int requiredQuantity = 5;

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.isQuantitySufficient(currentQuantity, requiredQuantity));
            assertEquals("Current quantity cannot be negative", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for negative required quantity")
        void shouldThrowExceptionForNegativeRequiredQuantity() {
            int currentQuantity = 10;
            int requiredQuantity = -5;

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.isQuantitySufficient(currentQuantity, requiredQuantity));
            assertEquals("Required quantity cannot be negative", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle large quantities")
        void shouldHandleLargeQuantities() {
            int currentQuantity = 1_000_000;
            int requiredQuantity = 999_999;

            boolean result = calculator.isQuantitySufficient(currentQuantity, requiredQuantity);

            assertTrue(result, "Should handle large quantities correctly");
        }
    }
}
