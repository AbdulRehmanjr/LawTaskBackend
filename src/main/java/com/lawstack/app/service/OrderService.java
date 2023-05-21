package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.Order;

public interface OrderService {
    Order saveOrder(Order order);

    Order getOrderById(String orderId);

    List<Order> getAllOrders();
    
    List<Order> getAllOrdersByUserId(String userId);

    List<Order> getAllOrdersByCustomerEmail(String email);
    
    void deleteOrder(String id);

    Order updateOrder(Order order);
}
