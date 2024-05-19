package main.webapp.Data.Models;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarDamage {
    private int id;
    private int orderId;
    private String description;
    private BigDecimal repairCost;
}
