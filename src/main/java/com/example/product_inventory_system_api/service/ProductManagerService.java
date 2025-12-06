package com.example.product_inventory_system_api.service;

import com.example.product_inventory_system_api.exception.ProductNotFoundException;
import com.example.product_inventory_system_api.model.Product;
import com.example.product_inventory_system_api.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProductManagerService {

    private final ProductRepository productRepository;

    public Product findProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product with SKU '%s' not found", sku)));
    }

    public Product restockProduct(String sku, int quantityToAdd) {
        if (quantityToAdd < 0) {
            throw new IllegalArgumentException("Quantity to add cannot be negative");
        }

        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku, "restock"));

        product.setQuantity(product.getQuantity() + quantityToAdd);
        return productRepository.save(product);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
