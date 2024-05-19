package main.webapp.Data.Models;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CarReturn {
    private int id;
    private int orderId;
    private Timestamp returnDate;
    private String carCondition;
    private String notes;
}

