package com.hrsvd.parkinglot.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ParkingTicket {
    private final String id;
    private final Vehicle vehicle;
    private final int floorNumber;
    private final String spotNumber;
    private final Instant entryTime;
    private Instant exitTime;
    public ParkingTicket(Vehicle vehicle, int floorNumber, String spotNumber, Instant entryTime) {
        this(UUID.randomUUID().toString(), vehicle, floorNumber, spotNumber, entryTime, null);
    }
    private ParkingTicket(String id, Vehicle vehicle, int floorNumber, String spotNumber, Instant entryTime, Instant exitTime) { this.id=id; this.vehicle=Objects.requireNonNull(vehicle); this.floorNumber=floorNumber; this.spotNumber=Objects.requireNonNull(spotNumber); this.entryTime=Objects.requireNonNull(entryTime); this.exitTime=exitTime; }
    public String id() { return id; } public Vehicle vehicle() { return vehicle; } public int floorNumber() { return floorNumber; } public String spotNumber() { return spotNumber; } public Instant entryTime() { return entryTime; } public Instant exitTime() { return exitTime; }
    public boolean isActive() { return exitTime == null; }
    public void close(Instant when) { if (!isActive()) throw new IllegalStateException("Ticket already closed"); if (when.isBefore(entryTime)) throw new IllegalArgumentException("Exit before entry"); exitTime = when; }
}
