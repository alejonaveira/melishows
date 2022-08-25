package com.meli.shows.model;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "theatres")
@Data
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String location;
    private String phone;
    private String email;
    private Boolean active;

    @Enumerated(EnumType.STRING)
    private TheatreCategory category;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    private List<Room> rooms;

}
