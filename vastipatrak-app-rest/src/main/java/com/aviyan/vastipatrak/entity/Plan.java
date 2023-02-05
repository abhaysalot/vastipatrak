package com.aviyan.vastipatrak.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "plan")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "plan_seq")
    @SequenceGenerator(name = "plan_seq", allocationSize = 5)
    private long id;

    private String name;
    private Integer groupLimit;
    private Integer familyLimit;
    private Integer loginLimit;
    private boolean fourEyeCheck;

}
