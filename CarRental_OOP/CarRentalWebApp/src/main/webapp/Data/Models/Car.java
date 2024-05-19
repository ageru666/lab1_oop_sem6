package main.webapp.Data.Models;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Car {
    private int id;
    private String brand;
    private String model;
    private boolean availability;
    private String imageURL;
    private BigDecimal priceForDay;
}

