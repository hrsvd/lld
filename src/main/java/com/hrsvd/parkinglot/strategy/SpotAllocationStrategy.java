package com.hrsvd.parkinglot.strategy;
import com.hrsvd.parkinglot.domain.ParkingFloor;
import com.hrsvd.parkinglot.domain.Vehicle;
import java.util.Collection;
import java.util.Optional;
public interface SpotAllocationStrategy { Optional<SpotAssignment> findSpot(Collection<ParkingFloor> floors, Vehicle vehicle); }
