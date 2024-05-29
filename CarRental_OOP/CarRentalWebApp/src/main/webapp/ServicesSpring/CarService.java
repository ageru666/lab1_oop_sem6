package main.webapp.ServicesSpring;

import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.Car;
import main.webapp.Data.UniversalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class CarService {

    private final UniversalDAO<Car> carDao;

    @Autowired
    public CarService(DatabaseConnection connectionProvider) throws SQLException {
        Connection connection = connectionProvider.getConnection();
        this.carDao = new UniversalDAO<>(connection, Car.class);
    }

    public List<Car> getAllCars() throws SQLException, InstantiationException, IllegalAccessException {
        return carDao.getAll();
    }

    public Car getCarById(int id) throws SQLException, InstantiationException, IllegalAccessException {
        return carDao.getById(id);
    }

    public void createCar(Car car) throws SQLException, IllegalAccessException {
        carDao.create(car);
    }

    public void updateCar(Car car) throws SQLException, IllegalAccessException {
        carDao.update(car);
    }

    public void deleteCar(int id) throws SQLException {
        carDao.delete(id);
    }
}

