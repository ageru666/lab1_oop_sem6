package main.webapp.Controllers;

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.Client;
import main.webapp.Data.Models.Login;
import main.webapp.Data.UniversalDAO;
import main.webapp.Utils.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "LoginServlet", value = "/api/login")
public class LoginServlet extends HttpServlet {
    private final UniversalDAO<Client> clientDAO;

    public LoginServlet() {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Login login = mapper.readValue(req.getInputStream(), Login.class);

        if (login.getUsername() == null || login.getUsername().isEmpty()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Missing parameters\"}");
            return;
        }

        try {
            Client foundClient = clientDAO.findBy("username", login.getUsername());
            if (foundClient == null) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"User not found\"}");
                return;
            }

            if (BCrypt.checkpw(login.getPassword(), foundClient.getPassword())) {
                String token = new JwtUtil().generateToken(login.getUsername(), foundClient.getRole());
                System.out.println(token);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write("{\"jwt\": \"" + token + "\", \"clientId\": " + foundClient.getId() + "}");
            } else {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\": \"Invalid password\"}");
            }

        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }
}


