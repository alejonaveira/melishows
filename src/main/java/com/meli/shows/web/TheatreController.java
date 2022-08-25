package com.meli.shows.web;

import com.meli.shows.model.Room;
import com.meli.shows.model.Seat;
import com.meli.shows.model.Show;
import com.meli.shows.model.Theatre;
import com.meli.shows.repository.RoomRepository;
import com.meli.shows.repository.SeatRepository;
import com.meli.shows.repository.TheatreRepository;
import com.meli.shows.service.MockBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.SynthesizedAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.directory.SearchResult;
import java.util.*;

@RestController
@RequestMapping(path = "/theatres")
public class TheatreController {

    Logger logger = LoggerFactory.getLogger(TheatreController.class);

    @Autowired
    private TheatreRepository repository;

    @Autowired
    private MockBuilder builder;

    @GetMapping
    public List<Theatre> all(){
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Theatre get(@PathVariable Long id){
        Optional<Theatre> t = this.repository.findById(id);
        if(!t.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No theatre found with requested id");
        return t.get();
    }


    @GetMapping("/{id}/rooms")
    public List<Room> getRooms(@PathVariable Long id){
        return this.repository.findById(id).get().getRooms();
    }


//    @PostMapping("/mock")
//    public String post(){
//        builder.buildTheatreRooms();
//        builder.buildSectionSeats();
//        return "OK";
//    }

}
