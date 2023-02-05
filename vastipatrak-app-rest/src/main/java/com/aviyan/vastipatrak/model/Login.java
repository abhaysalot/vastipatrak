package com.aviyan.vastipatrak.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Login {

    private long id;
    @NonNull
    private String loginId;
    @NonNull
    private String password;
    @NonNull
    private String userFirstName;
    @NonNull
    private String userLastName;
    @NonNull
    private String userEmail;

    private boolean active;

    private int failedAttempts;
    private Date firstLoginDate;
    private Date previousLoginDate;
    private Date lastLoginDate;
    private Date lastFailedLoginDate;

    @ToString.Exclude
    @JsonManagedReference
    private List<Role> roles;

    private String createdUser;
    private Date createdAt;
    private String modifiedUser;
    private Date modifiedAt;
}
