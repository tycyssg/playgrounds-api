package com.playgrounds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HtmlDto {
    private String title;
    private String description;
    private List<HtmlOpeningHours> openingHours;
    private String phone;
    private String email;
    private List<String> facilities;
    private String accessibility;
    private HtmlAddress address;
}
