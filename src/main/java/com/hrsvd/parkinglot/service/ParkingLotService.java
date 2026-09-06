package com.hrsvd.parkinglot.service;

import com.hrsvd.parkinglot.domain.*;
import com.hrsvd.parkinglot.pricing.PricingStrategy;
import com.hrsvd.parkinglot.repository.TicketRepository;
import com.hrsvd.parkinglot.strategy.SpotAllocationStrategy;
import com.hrsvd.parkinglot.strategy.SpotAssignment;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

/** Application-service boundary for parking entry, exit, and availability use cases. */
public final class ParkingLotService {
    private final Map<Integer, ParkingFloor> floors;
    private final SpotAllocationStrategy allocationStrategy;
    private final PricingStrategy pricingStrategy;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public ParkingLotService(Collection<ParkingFloor> floors, SpotAllocationStrategy allocationStrategy,
                             PricingStrategy pricingStrategy, TicketRepository ticketRepository, Clock clock) {
        Map<Integer, ParkingFloor> indexed = new TreeMap<>();
        for (ParkingFloor floor : floors) {
            if (indexed.put(floor.number(), floor) != null) throw new IllegalArgumentException("Duplicate floor number");
        }
        this.floors = Collections.unmodifiableMap(indexed);
        this.allocationStrategy = Objects.requireNonNull(allocationStrategy);
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy);
        this.ticketRepository = Objects.requireNonNull(ticketRepository);
        this.clock = Objects.requireNonNull(clock);
    }

    /** Synchronized to keep allocation and spot occupation consistent in this in-memory example. */
    public synchronized ParkingTicket park(Vehicle vehicle) {
        if (ticketRepository.findActiveByPlate(vehicle.plateNumber()).isPresent()) {
            throw new IllegalStateException("Vehicle already has an active ticket");
        }
        SpotAssignment assignment = allocationStrategy.findSpot(floors.values(), vehicle)
                .orElseThrow(() -> new IllegalStateException("No compatible parking spot available"));
        assignment.spot().park(vehicle);
        ParkingTicket ticket = new ParkingTicket(vehicle, assignment.floor().number(), assignment.spot().number(), Instant.now(clock));
        ticketRepository.save(ticket);
        return ticket;
    }

    public synchronized ParkingReceipt unpark(String ticketId) {
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown ticket: " + ticketId));
        if (!ticket.isActive()) throw new IllegalStateException("Ticket is already closed");
        ParkingFloor floor = Optional.ofNullable(floors.get(ticket.floorNumber()))
                .orElseThrow(() -> new IllegalStateException("Ticket references missing floor"));
        ParkingSpot spot = floor.spot(ticket.spotNumber())
                .orElseThrow(() -> new IllegalStateException("Ticket references missing spot"));
        Instant exitTime = Instant.now(clock);
        ticket.close(exitTime);
        spot.vacate(ticket.vehicle().plateNumber());
        var amount = pricingStrategy.calculate(ticket);
        ticketRepository.save(ticket);
        return new ParkingReceipt(ticket.id(), ticket.vehicle().plateNumber(), ticket.floorNumber(), ticket.spotNumber(),
                ticket.entryTime(), exitTime, amount);
    }

    public Map<SpotType, Long> availability(int floorNumber) {
        ParkingFloor floor = Optional.ofNullable(floors.get(floorNumber))
                .orElseThrow(() -> new IllegalArgumentException("Unknown floor: " + floorNumber));
        Map<SpotType, Long> result = new EnumMap<>(SpotType.class);
        for (SpotType type : SpotType.values()) result.put(type, floor.spots().stream()
                .filter(spot -> spot.type() == type && spot.parkedVehicle().isEmpty()).count());
        return Collections.unmodifiableMap(result);
    }
}
