package com.playgrounds.exceptions.models;

public class ParkNotFoundException extends Exception {

    public ParkNotFoundException(String message) {
        super(message);
    }
}
