package main.webapp.ServicesSpring;

import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.CarReturn;
import main.webapp.Data.UniversalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class CarReturnService {

    private final UniversalDAO<CarReturn> carReturnDao;

    @Autowired
    public CarReturnService(DatabaseConnection connectionProvider) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        this.carReturnDao = new UniversalDAO<>(connection, CarReturn.class);
    }

    public CarReturn getCarReturnByOrderId(int orderId) throws SQLException, InstantiationException, IllegalAccessException {
        return carReturnDao.getById(orderId, "orderid");
    }

    public void createCarReturn(CarReturn carReturn) throws SQLException, IllegalAccessException {
        carReturnDao.create(carReturn);
    }

    public void updateCarReturn(CarReturn carReturn) throws SQLException, IllegalAccessException {
        carReturnDao.update(carReturn);
    }

    public void deleteCarReturn(int id) throws SQLException {
        carReturnDao.delete(id);
    }
}
