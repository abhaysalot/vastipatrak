package com.aviyan.vastipatrak.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "person")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Person extends AbstractPersistingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "person_seq")
    @SequenceGenerator(name = "person_seq", allocationSize = 5)
    private long id;

    @Column(nullable = false)
    private String name;
    private String sex;
    private int age;
    private String phoneNumber;
    private String email;
    private String occupation;
    private String education;
    private String dharmikEducation;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonBackReference
    private Family family;
}
