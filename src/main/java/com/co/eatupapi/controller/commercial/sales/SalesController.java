package com.eatup.commercial.sales.controller;

import com.eatup.commercial.sales.model.SalesOrder;
import com.eatup.commercial.sales.service.SalesService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    /**
     * Retrieve all sales orders
     */
    @GetMapping
    public ResponseEntity<List<SalesOrder>> getAllSalesOrders() {
        List<SalesOrder> orders = salesService.getAllSalesOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieve a sales order by its ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<SalesOrder> getSalesOrderById(@PathVariable String orderId) {
        SalesOrder order = salesService.getSalesOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Create a new sales order
     */
    @PostMapping
    public ResponseEntity<SalesOrder> createSalesOrder(@RequestBody SalesOrder salesOrder) {
        SalesOrder createdOrder = salesService.createSalesOrder(salesOrder);
        return ResponseEntity.status(201).body(createdOrder);
    }

    /**
     * Cancel an existing sales order
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelSalesOrder(@PathVariable String orderId) {
        salesService.cancelSalesOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}