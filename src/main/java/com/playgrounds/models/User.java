package com.playgrounds.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotNull
    private String username;
    private String password;
    @Column(unique = true)
    @Email
    private String email;
    private String role;
    private String[] authorities;
    private Boolean isActive;
    private Boolean isLocked;
    private Date joinDate;
    @Transient
    private Long expiresIn;
    @Transient
    private String token;

}
