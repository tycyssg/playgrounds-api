package com.playgrounds.exceptions.models;

public class CountyNotFoundException extends Exception {

    public CountyNotFoundException() {
    }

    public CountyNotFoundException(String message) {
        super(message);
    }
}
