package com.pk.quizrooms.backend.enitity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Table(name="users")
@Entity
public class User {

    public enum Roles{
        ADMIN,USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @NotEmpty
    private String userName;
    @NotNull
    @NotEmpty
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull
    private User.Roles authority;
    @NotNull
    @NotEmpty
    private String firstName;
    private String middleName;
    @NotNull
    @NotEmpty
    private String lastName;
    private String phoneNumber;
    @NotNull
    private Date DOB;
    private String email;

}
