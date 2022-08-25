package com.meli.shows.service;

import com.meli.shows.dto.AvailableSeat;
import com.meli.shows.model.Event;
import com.meli.shows.model.EventCategory;
import com.meli.shows.model.Section;
import com.meli.shows.model.Show;
import com.meli.shows.repository.EventRepository;
import com.meli.shows.repository.SeatRepository;
import com.meli.shows.repository.ShowRepository;
import liquibase.pro.packaged.D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShowService {

    @Autowired
    private ShowRepository repository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    public List<AvailableSeat> findAvailableSeats(Long showId){
        // todo: jpql query to return seats and section eagerly

        return this.seatRepository.findAvailableSeats(showId).stream().map( seat -> {
            AvailableSeat dto = new AvailableSeat();
            dto.setId(seat.getId());
            dto.setRow(seat.getRow());
            dto.setIsle(seat.getIsle());
            dto.setPrice(this.seatRepository.findSectionPriceByShowIdAndSeatId(showId, seat.getId()).orElse(0.0));

            return dto;
        }).collect(Collectors.toList());
    }


}
