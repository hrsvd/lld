package com.hrsvd.parkinglot.strategy;
import com.hrsvd.parkinglot.domain.*;
import java.util.Collection;
import java.util.Optional;
public final class FirstAvailableSpotStrategy implements SpotAllocationStrategy {
    public Optional<SpotAssignment> findSpot(Collection<ParkingFloor> floors, Vehicle vehicle) {
        return floors.stream().sorted(java.util.Comparator.comparingInt(ParkingFloor::number))
                .flatMap(f -> f.spots().stream().filter(s -> s.canFit(vehicle)).map(s -> new SpotAssignment(f, s))).findFirst();
    }
}
