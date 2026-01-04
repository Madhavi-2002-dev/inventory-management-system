package com.example.inventorymanagementsystem.controller;

import com.example.inventorymanagementsystem.entity.Order;
import com.example.inventorymanagementsystem.service.OrderService;
import com.example.inventorymanagementsystem.repository.OrderRepository;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService,
                           OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    // ✅ PUBLIC (POSTMAN)
    @PostMapping
    public Order placeOrder(
            @RequestParam int productId,
            @RequestParam int quantity) {

        return orderService.placeOrder(productId, quantity);
    }

    @GetMapping("/orders/my")
    public List<Order> myOrders(Authentication auth) {
        return orderRepository.findByUserUsername(auth.getName());
    }

    // 🔒 ADMIN ONLY (FRONTEND)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
