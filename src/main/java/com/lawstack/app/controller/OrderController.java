package com.lawstack.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.model.Order;
import com.lawstack.app.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${web_domain}")
    private String Domain;

    @PostMapping("/register")
    public ResponseEntity<?> resgisterOrder(@RequestBody Order order) {

        Order n_order = this.orderService.saveOrder(order);

        if (n_order != null) {
            String URL = Domain+"/home/order-confirm?orderId=" + n_order.getId();
            return ResponseEntity.status(201).body(URL);
        }
        return ResponseEntity.status(401).body("Error in creation order");

    }

    @PostMapping("/register-confirmed")
    public ResponseEntity<?> OrderConfirmed(@RequestParam("requirement") MultipartFile file, String order) {

        Order n_order = new Order();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            n_order = objectMapper.readValue(order, Order.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return ResponseEntity.status(404).body("Error in creation order");
        }

        try {
            n_order.setRequirementFile(file.getBytes());
            n_order.setDocumentType(file.getContentType());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            ResponseEntity.status(404).body("Error in creation order");
        }

        n_order = this.orderService.updateOrder(n_order);
        if (n_order != null) {
            return ResponseEntity.status(201).body(n_order);
        }
        return ResponseEntity.status(404).body("Error in creation order");

    }

    @PostMapping("/done")
    ResponseEntity<?> jobDone(@RequestBody Order order) {
        log.info("Job completed updating its status");

        Order response = this.orderService.orderDone(order);

        if (response != null) {
            return ResponseEntity.status(201).body(order);
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {

        List<Order> result = this.orderService.getAllOrders();

        if (result == null) {
            log.error("Orders not found");
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(result);

    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") String orderId) {

        Order result = this.orderService.getOrderById(orderId);

        if (result == null) {
            log.error("Orders not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrderByUId(@PathVariable("userId") String userId) {

        List<Order> result = this.orderService.getAllOrdersByUserId(userId);

        if (result == null) {
            log.error("Orders not found");
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(result);

    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getOrderByEmail(@PathVariable("email") String email) {

        List<Order> result = this.orderService.getAllOrdersByCustomerEmail(email);

        if (result == null) {
            log.error("Orders not found");
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.status(201).body(result);

    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getOrderByCustomerId(@PathVariable("id") String id) {

        List<Order> result = this.orderService.getAllOrdersByCustomerId(id);

        if (result == null) {
            log.error("Orders not found");
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.status(201).body(result);

    }

    // delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") String id) {

        this.orderService.deleteOrder(id);
        return ResponseEntity.status(201).body("Deleted");
    }
}
