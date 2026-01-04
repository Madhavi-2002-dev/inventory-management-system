package com.example.inventorymanagementsystem.service;

import com.example.inventorymanagementsystem.entity.Order;
import com.example.inventorymanagementsystem.entity.Product;
import com.example.inventorymanagementsystem.entity.User;
import com.example.inventorymanagementsystem.repository.OrderRepository;
import com.example.inventorymanagementsystem.repository.ProductRepository;
import com.example.inventorymanagementsystem.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository=userRepository;
    }

    public Order placeOrder(int productId, int orderQuantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < orderQuantity) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setQuantity(product.getQuantity() - orderQuantity);
        productRepository.save(product);

        Order order = new Order();
        order.setProduct(product);
        order.setQuantity(orderQuantity);
        order.setTotalPrice(product.getPrice() * orderQuantity);

        return orderRepository.save(order);
    }


}
