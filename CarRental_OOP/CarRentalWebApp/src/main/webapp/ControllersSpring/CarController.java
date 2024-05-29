package main.webapp.ControllersSpring;
import main.webapp.Data.Models.Car;
import main.webapp.ServicesSpring.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getAllCars() throws SQLException, InstantiationException, IllegalAccessException {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable int id) throws SQLException, InstantiationException, IllegalAccessException {
        return carService.getCarById(id);
    }

    @PostMapping
    public void createCar(@RequestBody Car car) throws SQLException, IllegalAccessException {
        carService.createCar(car);
    }

    @PutMapping
    public void updateCar(@RequestBody Car car) throws SQLException, IllegalAccessException {
        carService.updateCar(car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable int id) throws SQLException {
        carService.deleteCar(id);
    }
}