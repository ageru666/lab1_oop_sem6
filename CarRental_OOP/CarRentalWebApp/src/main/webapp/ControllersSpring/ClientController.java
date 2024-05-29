package main.webapp.ControllersSpring;

import main.webapp.Data.Models.Client;
import main.webapp.ServicesSpring.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients() throws SQLException, InstantiationException, IllegalAccessException {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable int id) throws SQLException, InstantiationException, IllegalAccessException {
        return clientService.getClientById(id);
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) throws SQLException, IllegalAccessException {
        String hashedPassword = BCrypt.hashpw(client.getPassword(), BCrypt.gensalt());
        client.setPassword(hashedPassword);
        client.setRole("USER");
        return clientService.createClient(client);
    }

    @PutMapping
    public void updateClient(@RequestBody Client client) throws SQLException, IllegalAccessException {
        clientService.updateClient(client);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable int id) throws SQLException {
        clientService.deleteClient(id);
    }
}
