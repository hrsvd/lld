package com.hrsvd.parkinglot.service;

import java.math.BigDecimal;
import java.time.Instant;

public record ParkingReceipt(String ticketId, String vehiclePlate, int floorNumber, String spotNumber,
                             Instant entryTime, Instant exitTime, BigDecimal amount) { }
