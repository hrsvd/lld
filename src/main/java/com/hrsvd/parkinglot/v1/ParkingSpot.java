package com.hrsvd.parkinglot.v1;

public final class ParkingSpot {
    private final String id;
    private final VehicleType acceptedType;
    private Vehicle occupiedBy;

    public ParkingSpot(String id, VehicleType acceptedType) { this.id = id; this.acceptedType = acceptedType; }
    public boolean isAvailableFor(Vehicle vehicle) { return occupiedBy == null && acceptedType == vehicle.type(); }
    public void park(Vehicle vehicle) { if (!isAvailableFor(vehicle)) throw new IllegalStateException("Spot unavailable"); occupiedBy = vehicle; }
    public void vacate() { occupiedBy = null; }
    public String id() { return id; }
}
