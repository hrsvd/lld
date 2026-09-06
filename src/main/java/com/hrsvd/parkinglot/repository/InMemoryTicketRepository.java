package com.hrsvd.parkinglot.repository;

import com.hrsvd.parkinglot.domain.ParkingTicket;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryTicketRepository implements TicketRepository {
    private final ConcurrentMap<String, ParkingTicket> tickets = new ConcurrentHashMap<>();
    @Override public void save(ParkingTicket ticket) { tickets.put(ticket.id(), ticket); }
    @Override public Optional<ParkingTicket> findById(String ticketId) { return Optional.ofNullable(tickets.get(ticketId)); }
    @Override public Optional<ParkingTicket> findActiveByPlate(String plateNumber) {
        return tickets.values().stream().filter(ParkingTicket::isActive)
                .filter(ticket -> ticket.vehicle().plateNumber().equals(plateNumber)).findFirst();
    }
}
