package com.meli.shows.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String artist;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @OneToMany(mappedBy = "event")
    private List<Show> shows;
}
