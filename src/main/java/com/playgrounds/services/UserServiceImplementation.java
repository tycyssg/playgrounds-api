package com.playgrounds.services;

import com.playgrounds.exceptions.models.EmailExistException;
import com.playgrounds.exceptions.models.UsernameExistException;
import com.playgrounds.models.User;
import com.playgrounds.models.UserPrincipal;
import com.playgrounds.repository.UserRepository;
import com.playgrounds.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.playgrounds.constants.UserImplConstant.EMAIL_ALREADY_EXISTS;
import static com.playgrounds.constants.UserImplConstant.USERNAME_ALREADY_EXISTS;
import static com.playgrounds.enumeration.Role.ROLE_USER;


@Service
@Qualifier("UserDetailsService")
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByEmail(username);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return new UserPrincipal(user);
    }

    @Override
    public User login(User user) {
        return findUserByEmail(user.getEmail());
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User register(User user) throws EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(user.getUsername(), user.getEmail());

        String encodedPassword = encodePassword(user.getPassword());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setIsActive(true);
        user.setIsLocked(false);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());

        return userRepository.save(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateNewUsernameAndEmail(String newUsername, String newEmail) throws EmailExistException, UsernameExistException {

        if (userRepository.existsByEmail(newEmail)) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByUsername(newUsername)) {
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }

    }

}
