package com.aviyan.vastipatrak.model;

import com.aviyan.vastipatrak.constant.RoleName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Role {

    private long id;

    @NonNull
    private RoleName roleName;

    private boolean active;

    //Primary proprietor for a given login
    private boolean primaryRole;

    @JsonBackReference
    private Login login;

    private String createdUser;
    private Date createdAt;
    private String modifiedUser;
    private Date modifiedAt;

    private Group group;
}
