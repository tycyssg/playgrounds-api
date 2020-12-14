package com.playgrounds.services.interfaces;

import com.playgrounds.exceptions.models.EmailExistException;
import com.playgrounds.exceptions.models.UsernameExistException;
import com.playgrounds.models.User;


public interface UserService {

    public User login(User user) throws Exception;

    public User findUserByEmail(String email);

    User findUserByUsername(String username);

    User register(User user) throws EmailExistException, UsernameExistException;
}
