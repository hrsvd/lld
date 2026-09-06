package com.hrsvd.parkinglot.pricing;

import com.hrsvd.parkinglot.domain.ParkingTicket;
import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculate(ParkingTicket ticket);
}
