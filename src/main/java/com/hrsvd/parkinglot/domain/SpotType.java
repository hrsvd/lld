package com.hrsvd.parkinglot.domain;

public enum SpotType {
    MOTORCYCLE, COMPACT, LARGE;
    public boolean supports(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> true;
            case CAR -> this == COMPACT || this == LARGE;
            case TRUCK -> this == LARGE;
        };
    }
}
