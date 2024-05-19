package main.webapp.Controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.webapp.Data.DatabaseConnection;
import main.webapp.Data.Models.CarAvailable;
import main.webapp.Data.Models.Client;
import main.webapp.Data.UniversalDAO;
import main.webapp.Data.Models.Car;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CarServlet", value = "/api/cars")
public class CarServlet extends HttpServlet {
    private final UniversalDAO<Car> carDAO;

    public CarServlet() {
        this.carDAO = new UniversalDAO<>(getConnection(), Car.class);
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
        try {
            List<Car> cars = carDAO.getAll();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(cars));

        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Attempting to add new car");

            ObjectMapper mapper = new ObjectMapper();
            Car newCar = mapper.readValue(req.getInputStream(), Car.class);
            System.out.println(newCar);

            if (newCar == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }

            carDAO.create(newCar);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String carId = req.getParameter("carId");
        boolean updAvailable = Boolean.parseBoolean(req.getParameter("updAvailable"));

        if (req.getParameter("updAvailable") != null) {
            try {
                System.out.println("Attempting to update car availabce");

                ObjectMapper mapper = new ObjectMapper();
                CarAvailable carAvailableDTO = mapper.readValue(req.getInputStream(), CarAvailable.class);
                System.out.println(carAvailableDTO);

                if (carAvailableDTO == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                    return;
                }

                Car carToUpdate = carDAO.getById(carAvailableDTO.getId());
                carToUpdate.setAvailability(!carAvailableDTO.getStatus().equals("confirmed"));
                carDAO.update(carToUpdate);

                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException | IllegalAccessException | NumberFormatException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                System.out.println("Attempting to update car");

                ObjectMapper mapper = new ObjectMapper();
                Car carToUpdate = mapper.readValue(req.getInputStream(), Car.class);
                System.out.println(carToUpdate);

                if (carToUpdate == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                    return;
                }
                carToUpdate.setId(Integer.parseInt(carId));
                carDAO.update(carToUpdate);

                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException | IllegalAccessException | NumberFormatException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
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
            carDAO.delete(Integer.parseInt(carId));

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
