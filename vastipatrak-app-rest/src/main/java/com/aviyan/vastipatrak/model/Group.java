package com.aviyan.vastipatrak.model;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Group {

    private long id;

    @NonNull
    private String name;

    @NonNull
    private String email;
    private String phoneNumber;
    private String address;

    private Plan subscriptionPlan;
    private Date subscriptionStartDate;
    private Date subscriptionEndDate;

    private boolean active;

    private String createdUser;
    private Date createdAt;
    private String modifiedUser;
    private Date modifiedAt;

}
