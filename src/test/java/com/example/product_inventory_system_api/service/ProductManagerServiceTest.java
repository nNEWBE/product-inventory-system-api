package com.example.product_inventory_system_api.service;

import com.example.product_inventory_system_api.exception.ProductNotFoundException;
import com.example.product_inventory_system_api.model.Product;
import com.example.product_inventory_system_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductManagerServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductManagerService productManagerService;

    private Product testProduct;
    private static final String TEST_SKU = "TEST-SKU-001";
    private static final String TEST_NAME = "Test Product";
    private static final double TEST_PRICE = 99.99;
    private static final int TEST_QUANTITY = 50;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku(TEST_SKU);
        testProduct.setName(TEST_NAME);
        testProduct.setPrice(TEST_PRICE);
        testProduct.setQuantity(TEST_QUANTITY);
        testProduct.setDescription("A test product for unit testing");
    }

    @Nested
    @DisplayName("findProductBySku Tests")
    class FindProductBySkuTests {

        @Test
        @DisplayName("Should return product when SKU exists")
        void shouldReturnProductWhenSkuExists() {
            when(productRepository.findBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));

            Product result = productManagerService.findProductBySku(TEST_SKU);

            assertNotNull(result, "Product should not be null");
            assertEquals(TEST_SKU, result.getSku(), "SKU should match");
            assertEquals(TEST_NAME, result.getName(), "Name should match");
            assertEquals(TEST_PRICE, result.getPrice(), "Price should match");
            assertEquals(TEST_QUANTITY, result.getQuantity(), "Quantity should match");

            verify(productRepository, times(1)).findBySku(TEST_SKU);
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException when SKU does not exist")
        void shouldThrowExceptionWhenSkuNotFound() {
            String nonExistentSku = "NON-EXISTENT-SKU";
            when(productRepository.findBySku(nonExistentSku)).thenReturn(Optional.empty());

            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productManagerService.findProductBySku(nonExistentSku),
                    "Should throw ProductNotFoundException for non-existent SKU");

            assertTrue(exception.getMessage().contains(nonExistentSku),
                    "Exception message should contain the SKU");

            verify(productRepository, times(1)).findBySku(nonExistentSku);
        }

        @Test
        @DisplayName("Should return correct product details")
        void shouldReturnCorrectProductDetails() {
            Product detailedProduct = new Product(
                    "DETAILED-SKU",
                    "Detailed Product",
                    149.99,
                    100,
                    "A product with detailed information");
            detailedProduct.setId(2L);
            when(productRepository.findBySku("DETAILED-SKU")).thenReturn(Optional.of(detailedProduct));
            
            Product result = productManagerService.findProductBySku("DETAILED-SKU");

            assertAll("Product details should match",
                    () -> assertEquals("DETAILED-SKU", result.getSku()),
                    () -> assertEquals("Detailed Product", result.getName()),
                    () -> assertEquals(149.99, result.getPrice()),
                    () -> assertEquals(100, result.getQuantity()),
                    () -> assertEquals("A product with detailed information", result.getDescription()));
        }
    }

    @Nested
    @DisplayName("restockProduct Tests")
    class RestockProductTests {

        @Test
        @DisplayName("Should successfully restock product and update quantity")
        void shouldSuccessfullyRestockProduct() {
            int quantityToAdd = 25;
            int expectedNewQuantity = TEST_QUANTITY + quantityToAdd;

            when(productRepository.findBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Product result = productManagerService.restockProduct(TEST_SKU, quantityToAdd);

            assertNotNull(result, "Returned product should not be null");
            assertEquals(expectedNewQuantity, result.getQuantity(),
                    "Quantity should be updated correctly");

            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should call repository.save() exactly once with updated product")
        void shouldCallSaveExactlyOnceWithUpdatedProduct() {
            
            int quantityToAdd = 30;
            when(productRepository.findBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
            
            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

            productManagerService.restockProduct(TEST_SKU, quantityToAdd);

            verify(productRepository, times(1)).save(productCaptor.capture());

            Product savedProduct = productCaptor.getValue();
            assertEquals(TEST_SKU, savedProduct.getSku(), "Saved product SKU should match");
            assertEquals(TEST_QUANTITY + quantityToAdd, savedProduct.getQuantity(),
                    "Saved product should have updated quantity");
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException when restocking non-existent product")
        void shouldThrowExceptionWhenRestockingNonExistentProduct() {
            String nonExistentSku = "NON-EXISTENT-SKU";
            when(productRepository.findBySku(nonExistentSku)).thenReturn(Optional.empty());

            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productManagerService.restockProduct(nonExistentSku, 10),
                    "Should throw ProductNotFoundException for non-existent SKU");

            assertTrue(exception.getMessage().contains(nonExistentSku),
                    "Exception message should contain the SKU");

            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should correctly add zero quantity")
        void shouldHandleZeroQuantityAddition() {
            int quantityToAdd = 0;
            when(productRepository.findBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Product result = productManagerService.restockProduct(TEST_SKU, quantityToAdd);

            assertEquals(TEST_QUANTITY, result.getQuantity(),
                    "Quantity should remain unchanged when adding zero");
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for negative quantity")
        void shouldThrowExceptionForNegativeQuantity() {
            int negativeQuantity = -10;

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> productManagerService.restockProduct(TEST_SKU, negativeQuantity),
                    "Should throw IllegalArgumentException for negative quantity");

            assertEquals("Quantity to add cannot be negative", exception.getMessage());

            verify(productRepository, never()).findBySku(anyString());
            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should handle large quantity restock")
        void shouldHandleLargeQuantityRestock() {
            int quantityToAdd = 1_000_000;
            when(productRepository.findBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);


            verify(productRepository).save(productCaptor.capture());
            assertEquals(TEST_QUANTITY + quantityToAdd, productCaptor.getValue().getQuantity(),
                    "Should correctly handle large quantity additions");
        }

        @Test
        @DisplayName("Should preserve other product properties after restock")
        void shouldPreserveOtherPropertiesAfterRestock() {
            int quantityToAdd = 20;
            when(productRepository.findBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

            productManagerService.restockProduct(TEST_SKU, quantityToAdd);

            verify(productRepository).save(productCaptor.capture());
            Product savedProduct = productCaptor.getValue();

            assertAll("Other properties should be preserved",
                    () -> assertEquals(testProduct.getId(), savedProduct.getId()),
                    () -> assertEquals(testProduct.getSku(), savedProduct.getSku()),
                    () -> assertEquals(testProduct.getName(), savedProduct.getName()),
                    () -> assertEquals(testProduct.getPrice(), savedProduct.getPrice()),
                    () -> assertEquals(testProduct.getDescription(), savedProduct.getDescription()));
        }
    }

    @Nested
    @DisplayName("saveProduct Tests")
    class SaveProductTests {

        @Test
        @DisplayName("Should save and return product")
        void shouldSaveAndReturnProduct() {
            Product newProduct = new Product("NEW-SKU", "New Product", 49.99, 10, "New description");
            when(productRepository.save(newProduct)).thenReturn(newProduct);

            Product result = productManagerService.saveProduct(newProduct);

            assertNotNull(result);
            assertEquals("NEW-SKU", result.getSku());
            verify(productRepository, times(1)).save(newProduct);
        }
    }
}
