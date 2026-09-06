package com.hrsvd.parkinglot.v1;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class ParkingLot {
    private final List<ParkingSpot> spots;
    public ParkingLot(List<ParkingSpot> spots) { this.spots = spots; }
    public Ticket park(Vehicle vehicle) {
        ParkingSpot spot = spots.stream().filter(s -> s.isAvailableFor(vehicle)).findFirst()
                .orElseThrow(() -> new IllegalStateException("No compatible spot"));
        spot.park(vehicle);
        return new Ticket(UUID.randomUUID().toString(), vehicle, spot, Instant.now());
    }
    public long unpark(Ticket ticket) { ticket.spot().vacate(); return 20; }
}
