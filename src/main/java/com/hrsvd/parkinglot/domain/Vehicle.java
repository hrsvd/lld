package com.hrsvd.parkinglot.domain;

import java.util.Objects;
public record Vehicle(String plateNumber, VehicleType type) {
    public Vehicle { Objects.requireNonNull(plateNumber); Objects.requireNonNull(type); }
}
