package com.playgrounds.exceptions.models;

public class EmailExistException extends Exception {

    public EmailExistException(String message) {
        super(message);
    }
}
