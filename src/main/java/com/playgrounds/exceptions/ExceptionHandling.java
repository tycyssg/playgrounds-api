package com.playgrounds.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.playgrounds.exceptions.models.*;
import com.playgrounds.models.HttpCustomResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.playgrounds.constants.ErrorConstants.*;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(CountiesNotFoundException.class)
    public ResponseEntity<HttpCustomResponse> countiesNotFoundException() {
        return createHttpResponse(NOT_FOUND, COUNTIES_DATA_NOT_FOUND);
    }

    @ExceptionHandler(CountyNotFoundException.class)
    public ResponseEntity<HttpCustomResponse> countyNotFoundException() {
        return createHttpResponse(NOT_FOUND, COUNTY_DATA_NOT_FOUND);
    }

    @ExceptionHandler(ParkNotFoundException.class)
    public ResponseEntity<HttpCustomResponse> parkNotFoundException(ParkNotFoundException e) {
        return createHttpResponse(NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ParksDataNotFoundException.class)
    public ResponseEntity<HttpCustomResponse> parksDataNotFoundException() {
        return createHttpResponse(NOT_FOUND, PARKS_DATA_NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpCustomResponse> illegalStateException() {
        return createHttpResponse(NOT_IMPLEMENTED, UNEXPECTED_VALUE);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpCustomResponse> accountDisabledException() {
        return createHttpResponse(FORBIDDEN, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpCustomResponse> accountLockedException() {
        return createHttpResponse(LOCKED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpCustomResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpCustomResponse> tokenExpiredException(TokenExpiredException e) {
        return createHttpResponse(UNAUTHORIZED, e.getMessage());
    }


    @ExceptionHandler(InvalidDataFormatException.class)
    public ResponseEntity<HttpCustomResponse> invalidDataFormat() {
        return createHttpResponse(BAD_REQUEST, INVALID_DATA_FORMAT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpCustomResponse> badCredentialsException() {
        return createHttpResponse(BAD_REQUEST, INVALID_CREDENTIALS);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpCustomResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpCustomResponse> internalServerErrorException() {
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }


    private ResponseEntity<HttpCustomResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpCustomResponse httpCustomResponse = new HttpCustomResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message);

        return new ResponseEntity<>(httpCustomResponse, httpStatus);
    }

}
