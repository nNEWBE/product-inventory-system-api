package com.example.product_inventory_system_api.controller;

import com.example.product_inventory_system_api.model.Product;
import com.example.product_inventory_system_api.service.ProductManagerService;
import com.example.product_inventory_system_api.util.ProductCalculator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductManagerService productService;
    private final ProductCalculator productCalculator;

    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        Product product = productService.findProductBySku(sku);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PatchMapping("/{sku}/restock")
    public ResponseEntity<Product> restockProduct(
            @PathVariable String sku,
            @RequestBody Map<String, Integer> request) {
        int quantityToAdd = request.getOrDefault("quantityToAdd", 0);
        Product updatedProduct = productService.restockProduct(sku, quantityToAdd);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/calculate/discount")
    public ResponseEntity<Map<String, Object>> calculateDiscountedPrice(
            @RequestParam double originalPrice,
            @RequestParam double discountRate) {
        double discountedPrice = productCalculator.calculateDiscountedPrice(originalPrice, discountRate);
        return ResponseEntity.ok(Map.of(
                "originalPrice", originalPrice,
                "discountRate", discountRate,
                "discountedPrice", discountedPrice));
    }

    @GetMapping("/check/quantity")
    public ResponseEntity<Map<String, Object>> checkQuantitySufficient(
            @RequestParam int currentQuantity,
            @RequestParam int requiredQuantity) {
        boolean isSufficient = productCalculator.isQuantitySufficient(currentQuantity, requiredQuantity);
        return ResponseEntity.ok(Map.of(
                "currentQuantity", currentQuantity,
                "requiredQuantity", requiredQuantity,
                "isSufficient", isSufficient));
    }
}
