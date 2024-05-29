package main.webapp.ServicesSpring;

import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.CarDamage;
import main.webapp.Data.UniversalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class CarDamageService {

    private final UniversalDAO<CarDamage> carDamageDao;

    @Autowired
    public CarDamageService(DatabaseConnection connectionProvider) throws SQLException {
        Connection connection = connectionProvider.getConnection();
        this.carDamageDao = new UniversalDAO<>(connection, CarDamage.class);
    }

    public List<CarDamage> getAllByOrderId(int orderId) throws SQLException, InstantiationException, IllegalAccessException {
        return carDamageDao.getAllById(orderId, "orderid");
    }

    public void createCarDamage(CarDamage carDamage) throws SQLException, IllegalAccessException {
        carDamageDao.create(carDamage);
    }

    public void updateCarDamage(CarDamage carDamage) throws SQLException, IllegalAccessException {
        carDamageDao.update(carDamage);
    }

    public void deleteCarDamage(int id) throws SQLException {
        carDamageDao.delete(id);
    }
}
