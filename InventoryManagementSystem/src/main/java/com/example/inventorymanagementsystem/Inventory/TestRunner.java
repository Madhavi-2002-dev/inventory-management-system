package com.example.inventorymanagementsystem.Inventory;

import com.example.inventorymanagementsystem.entity.Product;

import com.example.inventorymanagementsystem.repository.ProductRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {
    private final ProductRepository productRepository;

    public TestRunner(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Comment out or remove this block
        /*
        Product p = new Product();
        p.setName("Laptop");
        p.setCategory("Electronics");
        p.setPrice(75000.0);
        p.setQuantity(10);
        productRepository.save(p);
        */
    }
}
