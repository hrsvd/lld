package com.hrsvd.parkinglot.pricing;

import com.hrsvd.parkinglot.domain.ParkingTicket;
import com.hrsvd.parkinglot.domain.VehicleType;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

/** Charges at least one hour and rounds partial hours upward. */
public final class HourlyPricingStrategy implements PricingStrategy {
    private final Map<VehicleType, BigDecimal> hourlyRates;
    public HourlyPricingStrategy(Map<VehicleType, BigDecimal> hourlyRates) { this.hourlyRates = Map.copyOf(hourlyRates); }
    @Override public BigDecimal calculate(ParkingTicket ticket) {
        if (ticket.isActive()) throw new IllegalArgumentException("Ticket must be closed before pricing");
        long minutes = Duration.between(ticket.entryTime(), ticket.exitTime()).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60);
        BigDecimal rate = hourlyRates.get(ticket.vehicle().type());
        if (rate == null) throw new IllegalArgumentException("No rate for " + ticket.vehicle().type());
        return rate.multiply(BigDecimal.valueOf(hours));
    }
}
