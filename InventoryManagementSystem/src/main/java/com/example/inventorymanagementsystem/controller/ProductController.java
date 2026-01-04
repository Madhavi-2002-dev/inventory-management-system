package com.example.inventorymanagementsystem.controller;

import com.example.inventorymanagementsystem.entity.Product;
import com.example.inventorymanagementsystem.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping("/out-of-stock")
    public List<Product> getOutOfStockProducts() {
        return productRepository.findByQuantity(0);
    }

    // 🟡 LOW STOCK PRODUCTS (1–5)
    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts() {
        return productRepository.findByQuantityBetween(1, 11);
    }
    // CREATE
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // READ ALL
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productRepository.findById(id).orElse(null);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            return productRepository.save(product);
        }).orElse(null);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productRepository.deleteById(id);
    }
 

}
