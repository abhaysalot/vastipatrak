package com.aviyan.vastipatrak.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "monk")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Monk extends AbstractPersistingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "monk_seq")
    @SequenceGenerator(name = "monk_seq", allocationSize = 5)
    private long id;

    @Column(nullable = false)
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

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonBackReference
    private Family family;
}
