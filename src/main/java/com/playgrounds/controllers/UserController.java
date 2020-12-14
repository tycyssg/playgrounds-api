package com.playgrounds.controllers;


import com.playgrounds.exceptions.ExceptionHandling;
import com.playgrounds.exceptions.models.EmailExistException;
import com.playgrounds.exceptions.models.UsernameExistException;
import com.playgrounds.models.User;
import com.playgrounds.models.UserPrincipal;
import com.playgrounds.services.interfaces.UserService;
import com.playgrounds.utility.JWTTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.zip.DataFormatException;

import static com.playgrounds.constants.ErrorConstants.INVALID_DATA_FORMAT;
import static com.playgrounds.constants.SecurityConstants.EXPIRATION_TIME;


@RestController
@RequestMapping("/api")
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;


    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<User> register(@Valid @RequestBody User user, BindingResult bindingResult) throws UsernameExistException, EmailExistException, DataFormatException {

        if (bindingResult.hasErrors()) throw new DataFormatException(INVALID_DATA_FORMAT);

        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getEmail(), user.getPassword());
        User loggedUser = userService.findUserByEmail(user.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loggedUser);

        loggedUser.setPassword(StringUtils.EMPTY);
        loggedUser.setExpiresIn(EXPIRATION_TIME);
        loggedUser.setToken(jwtTokenProvider.generateJwtToken(userPrincipal));

        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }


    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
