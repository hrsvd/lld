package com.hrsvd.parkinglot.service;

import com.hrsvd.parkinglot.domain.*;
import com.hrsvd.parkinglot.pricing.HourlyPricingStrategy;
import com.hrsvd.parkinglot.repository.InMemoryTicketRepository;
import com.hrsvd.parkinglot.strategy.FirstAvailableSpotStrategy;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ParkingLotServiceTest {
    @Test void parksCompatibleVehicleAndReleasesSpotWithRoundedHourlyCharge() {
        Instant entry = Instant.parse("2026-01-01T10:00:00Z");
        MutableClock clock = new MutableClock(entry);
        ParkingLotService lot = service(clock);
        ParkingTicket ticket = lot.park(new Vehicle("KA-01-AA-0001", VehicleType.CAR));
        assertEquals("C1", ticket.spotNumber());
        assertEquals(0, lot.availability(1).get(SpotType.COMPACT));
        clock.set(entry.plus(Duration.ofMinutes(61)));
        ParkingReceipt receipt = lot.unpark(ticket.id());
        assertEquals(new BigDecimal("40"), receipt.amount());
        assertEquals(1, lot.availability(1).get(SpotType.COMPACT));
    }
    @Test void rejectsSecondActiveTicketForSameVehicle() {
        ParkingLotService lot = service(new MutableClock(Instant.EPOCH));
        Vehicle vehicle = new Vehicle("KA-01-AA-0001", VehicleType.CAR);
        lot.park(vehicle);
        assertThrows(IllegalStateException.class, () -> lot.park(vehicle));
    }
    private static ParkingLotService service(Clock clock) {
        ParkingFloor floor = new ParkingFloor(1, List.of(new ParkingSpot("M1", SpotType.MOTORCYCLE), new ParkingSpot("C1", SpotType.COMPACT), new ParkingSpot("L1", SpotType.LARGE)));
        return new ParkingLotService(List.of(floor), new FirstAvailableSpotStrategy(),
                new HourlyPricingStrategy(Map.of(VehicleType.MOTORCYCLE, new BigDecimal("10"), VehicleType.CAR, new BigDecimal("20"), VehicleType.TRUCK, new BigDecimal("30"))),
                new InMemoryTicketRepository(), clock);
    }
    private static final class MutableClock extends Clock {
        private Instant instant; MutableClock(Instant instant) { this.instant = instant; }
        void set(Instant instant) { this.instant = instant; }
        public ZoneId getZone() { return ZoneOffset.UTC; }
        public Clock withZone(ZoneId zone) { return this; }
        public Instant instant() { return instant; }
    }
}
