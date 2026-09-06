package com.hrsvd.parkinglot.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ParkingFloor {
    private final int number;
    private final List<ParkingSpot> spots;
    public ParkingFloor(int number, List<ParkingSpot> spots) { this.number = number; this.spots = List.copyOf(spots); }
    public int number() { return number; }
    public List<ParkingSpot> spots() { return spots; }
    public Optional<ParkingSpot> spot(String number) { return spots.stream().filter(s -> s.number().equals(number)).findFirst(); }
}
