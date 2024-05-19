package main.webapp.Data.Models;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Order {
    private int id;
    private int clientId;
    private int carId;
    private Timestamp rentalStart;
    private Timestamp rentalEnd;
    private String status;
    private BigDecimal totalPrice;
}

/*
<div>
                <label>Status:</label>
                <select value={status} onChange={(e) => setStatus(e.target.value)} required>
                    <option value="pending">Pending</option>
                    <option value="confirmed">Confirmed</option>
                    <option value="cancelled">Cancelled</option>
                </select>
            </div>
*/