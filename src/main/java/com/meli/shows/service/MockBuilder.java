package com.meli.shows.service;

import com.meli.shows.model.*;
import com.meli.shows.repository.RoomRepository;
import com.meli.shows.repository.SeatRepository;
import com.meli.shows.repository.ShowRepository;
import com.meli.shows.repository.TheatreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class MockBuilder {

    private final Logger logger = LoggerFactory.getLogger(MockBuilder.class);

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowRepository showRepository;


    private final Integer MIN_ROWS = 3;
    private final Integer MAX_ROWS = 6;
    private final Integer MIN_ISLES = 4;
    private final Integer MAX_ISLES = 10;


    public void buildContext() {
        logger.info("Starting context initialization...");
        this.buildTheatreRooms();
        this.buildSectionSeats();
        logger.info("Context created");
    }


    public void buildSectionSeats(){
        List<Show> shows = this.showRepository.findAll();

        shows.stream().forEach(show -> {

            this.logger.info("Creating Section Seats for {}, {}", show.getEvent().getName(), show.getDate());

            if(show.getSections().size() > 1){
                List<Section> sections = show.getSections();
                if(show.getSections().size() > 1){
                    sections.forEach(section -> {
                        List<String> sectionRows;
                        if(section.getName().contains("VIP")){
                            sectionRows = Arrays.asList("A", "B");
                        }else if(section.getName().contains("Premiun")){
                            sectionRows = Arrays.asList("C", "D", "E");
                        }else{
                            sectionRows = Arrays.stream("FGHIJKLMNO".split("")).collect(Collectors.toList());
                        }
                        List<Seat> seats = this.seatRepository.findByRowInAndRoom_Id(sectionRows, show.getRoom().getId());
                        this.logger.info("Assigning {} seats for section {}, rows {}", seats.size(), section.getName(), String.join(", ", sectionRows));
                        section.getSeats().addAll(seats);
                    });
                }else{
                    sections.forEach(section -> {
                        List<Seat> seats;
                        List<String> premiunRows = Arrays.asList("A", "B", "C");
                        if(section.getName().contains("Premiun")){
                            seats = this.seatRepository.findByRowInAndRoom_Id(premiunRows, show.getRoom().getId());
                        }else{
                            seats = this.seatRepository.findByRowNotInAndRoom_Id(premiunRows, show.getRoom().getId());
                        }
                        this.logger.info("Assigning {} seats for section {}, rows {}", seats.size(), section.getName(), String.join(", ", premiunRows));
                        section.getSeats().addAll(seats);
                    });
                }

            }else{
                show.getSections().forEach(section -> {
                    List<Seat> seats = this.seatRepository.findByRoom_Id(show.getRoom().getId());
                    this.logger.info("Assigning ALL seats for section {}", seats.size(), section.getName());
                    section.getSeats().addAll((seats));
                });
            }
        });

        this.showRepository.saveAll(shows);
    }

    /**
     * Generates all the seats for each room in all the theatres
     */
    public void buildTheatreRooms(){
        List<Theatre> all = this.theatreRepository.findAll();
        all.stream().forEach(theatre -> {
            theatre.getRooms().stream().forEach(room -> {
                this.generateRoomSeats(room);
            });
        });

        this.theatreRepository.saveAll(all);
    }

    /**
     * Generates all the seats for a theatre room, given the number of rows and isles.
     * @param room
     * @param maxRows
     * @param maxIsles
     */
    private void generateRoomSeats(Room room, Integer maxRows, Integer maxIsles){
        List<Seat> seats = new ArrayList<Seat>();

        char[] rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();


        for (int i = 0; i < maxRows; i++) {
            for (int j = 1; j < maxIsles; j++) {
                Seat s = new Seat();
                s.setRow(String.valueOf(rows[i]));
                s.setIsle(Long.valueOf(j));
                s.setRoom(room);
                seats.add(s);
            }
        }
        room.setRows(maxRows);
        room.setIsles(maxIsles);
        room.setSeats(seats);
        this.logger.info("Building Room {} with {} rows and {} isles.", room.getName(), room.getRows(), room.getIsles());
    }

    /**
     * Overload that randomizes seat count.
     * @param room
     */
    private void generateRoomSeats(Room room){
        Integer maxRows = ThreadLocalRandom.current().nextInt(MIN_ROWS, MAX_ROWS + 1);
        Integer maxIsles = ThreadLocalRandom.current().nextInt(MIN_ISLES, MAX_ISLES/2) *2;
        this.generateRoomSeats(room, maxRows, maxIsles);
    }
}
