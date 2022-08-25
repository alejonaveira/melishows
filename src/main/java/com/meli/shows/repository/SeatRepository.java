package com.meli.shows.repository;

import com.meli.shows.model.Seat;
import com.meli.shows.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByRoom_Id(Long id);

    List<Seat> findByRowAndIsle(String row, Long isle);

    List<Seat> findByRowInAndRoom_Id(Collection<String> rows, Long id);

    List<Seat> findByRowNotInAndRoom_Id(Collection<String> rows, Long id);


    @Query(
            value = "SELECT sec.price FROM section_seats ss " +
                    "INNER JOIN sections sec ON ss.section_id = sec.id " +
                    "WHERE sec.show_id = ?1 " +
                    "AND ss.seat_id = ?2 ",
            nativeQuery = true)
    Optional<Double> findSectionPriceByShowIdAndSeatId(Long showId, Long seatId);


    @Query(
            value = "SELECT s.* FROM seats s " +
                    "INNER JOIN section_seats ss ON ss.seat_id = s.id " +
                    "INNER JOIN sections sec ON ss.section_id = sec.id " +
                    "WHERE sec.show_id = ?1 " +
                    "AND s.id NOT IN ( " +
                    "  SELECT s1.id FROM ticket_seats ts " +
                    "  INNER JOIN tickets t ON ts.ticket_id = t.id " +
                    "  INNER JOIN seats s1 ON ts.seat_id = s1.id " +
                    "  WHERE t.show_id = ?1" +
                    ") "+
                    "ORDER BY s.line, s.isle",
            nativeQuery = true)
    List<Seat> findAvailableSeats(Long show_id);



    @Query(value="SELECT s.* FROM seats s \n" +
            "INNER JOIN section_seats ss ON ss.seat_id = s.id \n" +
            "INNER JOIN sections sec ON ss.section_id = sec.id \n" +
            "WHERE sec.show_id = ?1 \n" +
            "ORDER BY s.line, s.isle",
            nativeQuery = true)
    List<Seat> findAllSeats(Long show_id);

}