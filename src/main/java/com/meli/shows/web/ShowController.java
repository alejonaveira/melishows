package com.meli.shows.web;

import com.meli.shows.model.Show;
import com.meli.shows.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowRepository repository;

    @GetMapping("/{id}")
    public Show get(@PathVariable Long id){
        Optional<Show> result = this.repository.findById(id);
        if(!result.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows found with requested id");
        return result.get();
    }


    @GetMapping("/")
    public List<Show> get(@RequestParam(required = false) Date fromDate, @RequestParam(required = false) Date toDate, @RequestParam(defaultValue = "id", required = true) String[] orderBy) {

        List<Sort.Order> orders = Arrays.stream(orderBy).map(field -> Sort.Order.asc(field)).collect(Collectors.toList());
        return this.repository.findAll(Sort.by(orders));
    }


}
