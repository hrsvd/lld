package com.hrsvd.parkinglot.domain;

import java.util.Objects;
import java.util.Optional;

public final class ParkingSpot {
    private final String number;
    private final SpotType type;
    private Vehicle vehicle;
    public ParkingSpot(String number, SpotType type) { this.number = Objects.requireNonNull(number); this.type = Objects.requireNonNull(type); }
    public String number() { return number; }
    public SpotType type() { return type; }
    public Optional<Vehicle> parkedVehicle() { return Optional.ofNullable(vehicle); }
    public boolean canFit(Vehicle candidate) { return vehicle == null && type.supports(candidate.type()); }
    public void park(Vehicle candidate) { if (!canFit(candidate)) throw new IllegalStateException("Spot cannot accept vehicle"); vehicle = candidate; }
    public void vacate(String plateNumber) { if (vehicle == null || !vehicle.plateNumber().equals(plateNumber)) throw new IllegalStateException("Vehicle does not occupy spot"); vehicle = null; }
}
