package com.example.inventorymanagementsystem.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventorymanagementsystem.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	 // Find products by name
    List<Product> findByName(String name);
    
   // Find products by category
    List<Product> findByCategory(String category);
    
    List<Product> findByQuantity(int quantity);

    // Low stock (quantity between 1 and 5)
    List<Product> findByQuantityBetween(int min, int max);
}
