package com.meli.shows.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "seats")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "line")
    private String row;

    private Long isle;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

//    @ManyToMany(mappedBy = "sectionSeats")
//    private Set<Section> sections;


//    @ManyToMany(mappedBy = "ticketSeats")
//    private Set<Section> tickets;

}
