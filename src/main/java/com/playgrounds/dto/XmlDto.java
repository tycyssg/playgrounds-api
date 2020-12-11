package com.playgrounds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlDto {
    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String phone;
    private String email;
    private String website;
    private String type;
    private String category;
    private String openingHours;
    private String directions;
    private String surfaceType;
    private String comments;
    private String accessiblePlayItems;
    private String disabledParking;
    private String parkRanger;
    private String toilets;
    private String disabledToilets;
    private String babyChanging;
    private String seating;
    private String drinkingWater;
    private String lat;
    private String longitude;
}
