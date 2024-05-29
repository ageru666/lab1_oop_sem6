package main.webapp.ControllersSpring;

import main.webapp.Data.Models.CarDamage;
import main.webapp.ServicesSpring.CarDamageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/car-damage")
public class CarDamageController {
    private final CarDamageService carDamageService;

    @Autowired
    public CarDamageController(CarDamageService carDamageService) {
        this.carDamageService = carDamageService;
    }

    @GetMapping
    public ResponseEntity<?> getCarDamages(@RequestParam Integer orderId) {
        try {
            if (orderId == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"Missing parameters\"}");
            }

            List<CarDamage> carDamages = carDamageService.getAllByOrderId(orderId);
            return ResponseEntity.ok(carDamages);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createCarDamage(@RequestBody CarDamage carDamage) {
        try {
            carDamageService.createCarDamage(carDamage);
            return ResponseEntity.status(201).build();
        } catch (SQLException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCarDamage(@RequestBody CarDamage carDamage) {
        try {
            carDamageService.updateCarDamage(carDamage);
            return ResponseEntity.ok().build();
        } catch (SQLException | IllegalAccessException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCarDamage(@RequestParam Integer carId) {
        try {
            if (carId == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"Missing parameters\"}");
            }
            carDamageService.deleteCarDamage(carId);
            return ResponseEntity.ok().build();
        } catch (SQLException | NumberFormatException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }
}
