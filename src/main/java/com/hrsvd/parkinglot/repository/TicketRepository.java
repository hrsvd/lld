package com.hrsvd.parkinglot.repository;

import com.hrsvd.parkinglot.domain.ParkingTicket;
import java.util.Optional;

public interface TicketRepository {
    void save(ParkingTicket ticket);
    Optional<ParkingTicket> findById(String ticketId);
    Optional<ParkingTicket> findActiveByPlate(String plateNumber);
}
