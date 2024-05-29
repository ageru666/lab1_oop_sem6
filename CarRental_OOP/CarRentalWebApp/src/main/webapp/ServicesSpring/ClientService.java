package main.webapp.ServicesSpring;

import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.Client;
import main.webapp.Data.UniversalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class ClientService {

    private final UniversalDAO<Client> clientDao;

    @Autowired
    public ClientService(DatabaseConnection connectionProvider) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        this.clientDao = new UniversalDAO<>(connection, Client.class);
    }

    public List<Client> getAllClients() throws SQLException, InstantiationException, IllegalAccessException {
        return clientDao.getAll();
    }

    public Client getClientById(int id) throws SQLException, InstantiationException, IllegalAccessException {
        return clientDao.getById(id);
    }

    public Client createClient(Client client) throws SQLException, IllegalAccessException {
        clientDao.create(client);
        client.setPassword(null); // Удалите пароль перед отправкой клиенту
        return client;
    }

    public void updateClient(Client client) throws SQLException, IllegalAccessException {
        clientDao.update(client);
    }

    public void deleteClient(int id) throws SQLException {
        clientDao.delete(id);
    }
}
