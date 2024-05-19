package main.webapp.Controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.CarDamage;
import main.webapp.Data.Models.CarReturn;
import main.webapp.Data.UniversalDAO;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "CarReturnServlet", value = "/api/car-return")
public class CarReturnServlet extends HttpServlet {
    private final UniversalDAO<CarReturn> carReturnDAO;

    public CarReturnServlet() {
        this.carReturnDAO = new UniversalDAO<>(getConnection(), CarReturn.class);
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

            CarReturn carReturn = carReturnDAO.getById(Integer.parseInt(orderID), "orderid");
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(carReturn));
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to add new return");

            ObjectMapper mapper = new ObjectMapper();
            CarReturn car = mapper.readValue(req.getInputStream(), CarReturn.class);
            System.out.println(car);

            if (car == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            carReturnDAO.create(car);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to upd return");

            ObjectMapper mapper = new ObjectMapper();
            CarReturn car = mapper.readValue(req.getInputStream(), CarReturn.class);
            System.out.println(car);

            if (car == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            carReturnDAO.update(car);

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
            carReturnDAO.delete(Integer.parseInt(carId));

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

