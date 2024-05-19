package main.webapp.Controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.CarReturn;
import main.webapp.Data.Models.Client;
import main.webapp.Data.Models.Order;
import main.webapp.Data.UniversalDAO;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClientServlet", value = "/api/clients")
public class ClientServlet extends HttpServlet {
    private final UniversalDAO<Client> clientDAO;

    public ClientServlet() {
        this.clientDAO = new UniversalDAO<>(getConnection(), Client.class);
    }

    private Connection getConnection() {
        try {
            return DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientIdParam = req.getParameter("clientId");
        try {
            List<Client> clients;
            if (clientIdParam != null) {
                int clientId = Integer.parseInt(clientIdParam);
                Client client = clientDAO.getById(clientId);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(new Gson().toJson(client));
            } else {
                clients = clientDAO.getAll();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(new Gson().toJson(clients));
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to register new user");

            ObjectMapper mapper = new ObjectMapper();
            Client newClient = mapper.readValue(req.getInputStream(), Client.class);
            System.out.println(newClient);

            if (newClient == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            String hashedPassword = BCrypt.hashpw(newClient.getPassword(), BCrypt.gensalt());
            newClient.setPassword(hashedPassword);
            newClient.setRole("USER");
            clientDAO.create(newClient);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            newClient.setPassword(null); // Удалите пароль перед отправкой клиенту
            resp.getWriter().write(new Gson().toJson(newClient));

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to upd Client");

            ObjectMapper mapper = new ObjectMapper();
            Client client = mapper.readValue(req.getInputStream(), Client.class);
            System.out.println(client);

            if (client == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            clientDAO.update(client);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to delete Client");

            ObjectMapper mapper = new ObjectMapper();
            Client client = mapper.readValue(req.getInputStream(), Client.class);
            System.out.println(client);

            if (client == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }
            clientDAO.delete(client.getId());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

