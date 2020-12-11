package com.playgrounds.services.interfaces;

import com.playgrounds.models.User;


public interface UserService {

    public User login(User user) throws Exception;

    public User findUserByEmail(String email);

}
