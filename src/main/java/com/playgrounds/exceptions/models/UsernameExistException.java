package com.playgrounds.exceptions.models;

public class UsernameExistException extends Exception {

    public UsernameExistException(String message) {
        super(message);
    }
}
