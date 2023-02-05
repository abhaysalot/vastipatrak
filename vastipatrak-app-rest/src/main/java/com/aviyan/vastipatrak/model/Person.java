package com.aviyan.vastipatrak.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Person {

    private long id;

    private String name;
    private String sex;
    private int age;
    private String phoneNumber;
    private String email;
    private String occupation;
    private String education;
    private String dharmikEducation;
    private boolean active;

    private Family family;
}
