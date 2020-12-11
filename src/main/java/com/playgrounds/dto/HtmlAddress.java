package com.playgrounds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HtmlAddress {
    private String address1;
    private String address2;
    private String dependentLocality;
    private String locality;
    private String administrativeArea;
    private String postalCode;
    private String country;
}
