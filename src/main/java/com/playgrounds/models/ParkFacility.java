package com.playgrounds.models;

import com.playgrounds.enumeration.Options;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PARKS_FACILITIES")
public class ParkFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parkId;
    private String facility;
    private String type;
    private String category;
    private String parkRanger;
    private String accessiblePlayItems;

    @Column(name = "drinking_water")
    @Enumerated(EnumType.STRING)
    private Options drinkingWater;

    @Column(name = "toilets")
    @Enumerated(EnumType.STRING)
    private Options toilets;

    public ParkFacility(String facility, String type, Options drinkingWater, Options toilets) {
        this.facility = facility;
        this.type = type;
        this.drinkingWater = drinkingWater;
        this.toilets = toilets;
    }
}
