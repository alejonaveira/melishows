package com.meli.shows.service;

import com.meli.shows.exception.NotFoundException;
import com.meli.shows.exception.SeatTakenException;
import com.meli.shows.model.Seat;
import com.meli.shows.model.Show;
import com.meli.shows.model.Ticket;
import com.meli.shows.repository.SeatRepository;
import com.meli.shows.repository.ShowRepository;
import com.meli.shows.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TicketRepository ticketRepository;


    public Ticket createTicket(Long show_id, List<Long> seat_ids, String dni, String name) throws NotFoundException, SeatTakenException {

        Optional<Show> show = this.showRepository.findById(show_id);

        if(!show.isPresent()){
            throw new NotFoundException("No show found with the supplied ID");
        }


        Ticket tkt = new Ticket();
        tkt.setDni(dni);
        tkt.setName(name);
        tkt.setShow(show.get());

        List<Seat> seats = this.seatRepository.findAllById(seat_ids);
        // avail check
        List<Seat> available = this.seatRepository.findAvailableSeats(show_id);
        if( !available.containsAll(seats)){
            throw new SeatTakenException("The requested seat is not available for this show");
        }
        tkt.setSeats(seats.stream().collect(Collectors.toSet()));

        Double price = seats.stream()
                .map(x -> this.seatRepository.findSectionPriceByShowIdAndSeatId(show_id, x.getId())
                        .get())
                .reduce(0.0, Double::sum);
        tkt.setPrice(price);
        this.ticketRepository.save(tkt);
        return tkt;
    }
}
