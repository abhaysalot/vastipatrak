package com.aviyan.vastipatrak.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Monk {

    private long id;

    private String name;
    private String previousName;
    private String sex;
    private int age;
    private String qualification;
    private String gaccha;
    private String phoneNumber;
    private String email;
    private Date dikshaDay;
    private boolean active;

    private Family family;
}
