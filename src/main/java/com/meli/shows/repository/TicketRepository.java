package com.meli.shows.repository;

import com.meli.shows.model.Seat;
import com.meli.shows.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}