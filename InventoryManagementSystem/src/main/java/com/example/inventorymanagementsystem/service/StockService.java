package com.example.inventorymanagementsystem.service;

import com.example.inventorymanagementsystem.entity.Product;
import com.example.inventorymanagementsystem.entity.Stock;
import com.example.inventorymanagementsystem.repository.ProductRepository;
import com.example.inventorymanagementsystem.repository.StockRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public StockService(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    // CREATE
    public Stock createStock(Stock stockRequest) {

        int productId = stockRequest.getProduct().getId();

        Product product = productRepository.findById( productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        // auto-update product quantity
        product.setQuantity(product.getQuantity() + stockRequest.getQuantity());
        productRepository.save(product);

        stockRequest.setProduct(product);
        stockRequest.setLastUpdated(LocalDateTime.now());

        return stockRepository.save(stockRequest);
    }


    // READ ALL
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // READ ONE
    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    // UPDATE
    public Stock updateStock(Long id, Stock data) {
        return stockRepository.findById(id).map(stock -> {
            stock.setQuantity(data.getQuantity());
            stock.setLastUpdated(LocalDateTime.now());
            return stockRepository.save(stock);
        }).orElse(null);
    }

    // DELETE
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}
