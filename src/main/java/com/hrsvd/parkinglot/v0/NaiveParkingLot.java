package com.hrsvd.parkinglot.v0;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Version 0 only: intentionally keeps all responsibilities in one class. */
public class NaiveParkingLot {
    private final int capacity;
    private final Map<String, Instant> activeTickets = new HashMap<>();

    public NaiveParkingLot(int capacity) {
        this.capacity = capacity;
    }

    public String park(String plateNumber) {
        if (activeTickets.size() == capacity) {
            throw new IllegalStateException("Parking lot is full");
        }
        String ticketId = UUID.randomUUID().toString();
        activeTickets.put(ticketId, Instant.now());
        return ticketId;
    }

    public long unpark(String ticketId) {
        Instant enteredAt = activeTickets.remove(ticketId);
        if (enteredAt == null) {
            throw new IllegalArgumentException("Unknown ticket: " + ticketId);
        }
        long hours = Math.max(1, (Instant.now().toEpochMilli() - enteredAt.toEpochMilli() + 3_599_999) / 3_600_000);
        return hours * 20; // pricing is hard-coded here too
    }

    public int availableSpaces() {
        return capacity - activeTickets.size();
    }
}
