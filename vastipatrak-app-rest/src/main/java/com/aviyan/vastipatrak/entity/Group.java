package com.aviyan.vastipatrak.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "group")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Group extends AbstractPersistingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "group_seq")
    @SequenceGenerator(name = "group_seq", allocationSize = 5)
    private long id;

    private String name;

    private String email;
    private String phoneNumber;
    private String address;

    @OneToMany(mappedBy = "group", fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<Family> families;

    @OneToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    private Plan subscriptionPlan;

    private Date subscriptionStartDate;
    private Date subscriptionEndDate;

    private boolean active;
}
