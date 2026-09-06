package com.hrsvd.parkinglot.strategy;
import com.hrsvd.parkinglot.domain.ParkingFloor;
import com.hrsvd.parkinglot.domain.ParkingSpot;
public record SpotAssignment(ParkingFloor floor, ParkingSpot spot) { }
