package main.webapp.Controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.Client;
import main.webapp.Data.Models.Order;
import main.webapp.Data.UniversalDAO;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "OrderServlet", value = "/api/orders")
public class OrderServlet extends HttpServlet {
    private final UniversalDAO<Order> orderDAO;

    public OrderServlet() {
        this.orderDAO = new UniversalDAO<>(getConnection(), Order.class);
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
            List<Order> orders;
            if (clientIdParam != null) {
                int clientId = Integer.parseInt(clientIdParam);
                orders = orderDAO.getAllByClientId(clientId);
            } else {
                orders = orderDAO.getAll();
            }

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(orders));
        } catch (SQLException | InstantiationException | IllegalAccessException | NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order;
        try {
            order = mapper.readValue(req.getInputStream(), Order.class);
            if (order != null) {
                orderDAO.create(order);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.setContentType("application/json");
                resp.getWriter().write(new Gson().toJson(order));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            }
        } catch (SQLException | IllegalAccessException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating order");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to upd Order");

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(req.getInputStream(), Order.class);

            if (order == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }
            System.out.println(order);

            orderDAO.update(order);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to delete Order");

            ObjectMapper mapper = new ObjectMapper();
            Order order = mapper.readValue(req.getInputStream(), Order.class);
            System.out.println(order);

            if (order == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }
            orderDAO.delete(order.getId());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

