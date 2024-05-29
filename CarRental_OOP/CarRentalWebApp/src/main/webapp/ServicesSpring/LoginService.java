package main.webapp.ServicesSpring;

import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.Client;
import main.webapp.Data.UniversalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class LoginService {

    private final UniversalDAO<Client> clientDao;

    @Autowired
    public LoginService(DatabaseConnection connectionProvider) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        this.clientDao = new UniversalDAO<>(connection, Client.class);
    }

    public Client findByUsername(String username) throws SQLException, InstantiationException, IllegalAccessException {
        return clientDao.findBy("username", username);
    }
}
