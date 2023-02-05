package com.aviyan.vastipatrak.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "login")
@Getter
@Setter
@ToString
public class Login extends AbstractPersistingEntity {

    public Login(){
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "login_seq")
    @SequenceGenerator(name = "login_seq", allocationSize = 5)
    private long id;

    private String loginId;
    private String password;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private int failedAttempts;
    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date firstLoginDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date previousLoginDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastFailedLoginDate;

    @ToString.Exclude
    @OneToMany(mappedBy = "login")
    private List<Role> roles;
}
