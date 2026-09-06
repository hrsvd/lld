package com.hrsvd.parkinglot.v1;

import java.time.Instant;

public record Ticket(String id, Vehicle vehicle, ParkingSpot spot, Instant enteredAt) { }
