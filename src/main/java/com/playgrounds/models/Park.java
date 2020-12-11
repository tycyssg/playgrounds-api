package com.playgrounds.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PARKS")
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long countyId;

    private String name;
    private String description;
    private String phone;
    private String email;
    private String website;
    private String accessibility;
    private String directions;
    private String surfaceType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parkId")
    private List<ParkFacility> parkFacilities;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parkId")
    private List<ParkOpeningHours> parkOpeningHours;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private ParkAddress parkAddress;
}
