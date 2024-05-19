package main.webapp.Controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.Car;
import main.webapp.Data.Models.CarDamage;
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
import java.util.List;

@WebServlet(name = "CarDamageServlet", value = "/api/car-damage")
public class CarDamageServlet extends HttpServlet {
    private final UniversalDAO<CarDamage> carDamageDAO;

    public CarDamageServlet() {
        this.carDamageDAO = new UniversalDAO<>(getConnection(), CarDamage.class);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("text/plain");
        response.getWriter().println(errorMessage);
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
        String orderID = req.getParameter("orderId");
        try {
            if (orderID == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            List<CarDamage> carDamages = carDamageDAO.getAllById(Integer.parseInt(orderID), "orderid");
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(carDamages));
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to add new carDamage");

            ObjectMapper mapper = new ObjectMapper();
            CarDamage dmg = mapper.readValue(req.getInputStream(), CarDamage.class);
            System.out.println(dmg);

            if (dmg == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            carDamageDAO.create(dmg);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to update carDamage");

            ObjectMapper mapper = new ObjectMapper();
            CarDamage dmg = mapper.readValue(req.getInputStream(), CarDamage.class);
            System.out.println(dmg);

            if (dmg == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            carDamageDAO.update(dmg);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String carId = req.getParameter("carId");
        try {
            System.out.println("Attempting to delete car");

            if (carId == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }
            carDamageDAO.delete(Integer.parseInt(carId));

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

