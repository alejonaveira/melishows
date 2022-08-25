package com.meli.shows.repository;

import com.meli.shows.model.Section;
import com.meli.shows.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {

    
    Optional<Section> findByIdAndSections_Seats_Id(Long showId, Long seatId);

    
}