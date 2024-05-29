package main.webapp.ControllersSpring;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.webapp.Data.Models.CarReturn;
import main.webapp.ServicesSpring.CarReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/car-return")
public class CarReturnController {
    private final CarReturnService carReturnService;

    @Autowired
    public CarReturnController(CarReturnService carReturnService) {
        this.carReturnService = carReturnService;
    }

    @GetMapping
    public ResponseEntity<?> getCarReturn(@RequestParam Integer orderId) {
        try {
            if (orderId == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"Missing parameters\"}");
            }

            CarReturn carReturn = carReturnService.getCarReturnByOrderId(orderId);
            return ResponseEntity.ok(carReturn);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createCarReturn(@RequestBody CarReturn carReturn) {
        try {
            carReturnService.createCarReturn(carReturn);
            return ResponseEntity.status(201).build();
        } catch (SQLException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCarReturn(@RequestBody CarReturn carReturn) {
        try {
            carReturnService.updateCarReturn(carReturn);
            return ResponseEntity.ok().build();
        } catch (SQLException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCarReturn(@RequestParam Integer carId) {
        try {
            if (carId == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"Missing parameters\"}");
            }
            carReturnService.deleteCarReturn(carId);
            return ResponseEntity.ok().build();
        } catch (SQLException | NumberFormatException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }
}
