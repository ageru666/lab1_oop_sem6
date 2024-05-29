package main.webapp.ServicesSpring;

import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.Order;
import main.webapp.Data.UniversalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class OrderService {

    private final UniversalDAO<Order> orderDao;

    @Autowired
    public OrderService(DatabaseConnection connectionProvider) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        this.orderDao = new UniversalDAO<>(connection, Order.class);
    }

    public List<Order> getAllOrders() throws SQLException, InstantiationException, IllegalAccessException {
        return orderDao.getAll();
    }

    public List<Order> getAllByClientId(int clientId) throws SQLException, InstantiationException, IllegalAccessException {
        return orderDao.getAllByClientId(clientId);
    }

    public void createOrder(Order order) throws SQLException, IllegalAccessException {
        orderDao.create(order);
    }

    public void updateOrder(Order order) throws SQLException, IllegalAccessException {
        orderDao.update(order);
    }

    public void deleteOrder(int id) throws SQLException {
        orderDao.delete(id);
    }
}
