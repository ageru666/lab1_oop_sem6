package main.webapp.ControllersSpring;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.webapp.Data.Models.Order;
import main.webapp.ServicesSpring.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam(required = false) Integer clientId) {
        try {
            List<Order> orders;
            if (clientId != null) {
                orders = orderService.getAllByClientId(clientId);
            } else {
                orders = orderService.getAllOrders();
            }
            return ResponseEntity.ok(orders);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            orderService.createOrder(order);
            return ResponseEntity.status(201).body(order);
        } catch (SQLException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Error creating order\"}");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody Order order) {
        try {
            orderService.updateOrder(order);
            return ResponseEntity.ok().build();
        } catch (SQLException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOrder(@RequestBody Order order) {
        try {
            orderService.deleteOrder(order.getId());
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }
}
