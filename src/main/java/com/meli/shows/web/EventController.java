package com.meli.shows.web;

import com.meli.shows.dto.AvailableSeat;
import com.meli.shows.exception.NotFoundException;
import com.meli.shows.exception.SeatTakenException;
import com.meli.shows.model.*;
import com.meli.shows.repository.EventRepository;
import com.meli.shows.repository.SeatRepository;
import com.meli.shows.service.MockBuilder;
import com.meli.shows.service.ShowService;
import com.meli.shows.service.SortHelper;
import com.meli.shows.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/events")
public class EventController {

    @Autowired
    private EventRepository repository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ShowService showService;


    @GetMapping
    public List<Event> all(
            @RequestParam(required = false) String eventCategory,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double fromPrice,
            @RequestParam(required = false) Double toPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(required = false) String[] sort

    ){
        List<EventCategory> categories = Arrays.asList(EventCategory.values());
        if(eventCategory != null ){
            categories = new ArrayList<>();
            try {
                categories.addAll(Arrays.stream(eventCategory.split(","))
                        .map(c -> EventCategory.valueOf(c.toUpperCase().strip()))
                        .collect(Collectors.toList())
                );
            }catch (NullPointerException | IllegalArgumentException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified event category does not exist");
            }
        }
        final String searchName = name != null ? '%'+ name.strip() + '%' : null;

        return this.repository.findByNameCategoryDatePrice(searchName, categories, fromDate, toDate, fromPrice, toPrice, SortHelper.convertFromString(sort));
    }

    @GetMapping("/{id}")
    public Event get(@PathVariable Long id){
        return this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No events found with requested id"));
    }

    @GetMapping("/{id}/shows/{showId}/seats")
    public List<Seat> getSeats(@PathVariable Long showId){
        return this.seatRepository.findAllSeats(showId);
    }

    @GetMapping("/{id}/shows/{showId}/available")
    public List<AvailableSeat> getAvailSeats(@PathVariable Long id, @PathVariable Long showId){
        return this.showService.findAvailableSeats(showId);
    }


    @PostMapping("/{id}/shows/{showId}/ticket")
    public Ticket createTicket(@PathVariable Long id, @PathVariable Long showId, @RequestBody Ticket ticket) {
        try {
            List<Long> ids = ticket.getSeats().stream().map(s -> s.getId()).collect(Collectors.toList());
            return ticketService.createTicket(showId, ids, ticket.getDni(), ticket.getName());
        }catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SeatTakenException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
