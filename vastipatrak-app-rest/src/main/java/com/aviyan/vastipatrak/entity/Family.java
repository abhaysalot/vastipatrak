package com.aviyan.vastipatrak.entity;

import com.aviyan.vastipatrak.constant.IncomeRange;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "family")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Family extends AbstractPersistingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "family_seq")
    @SequenceGenerator(name = "family_seq", allocationSize = 5)
    private long id;

    @Column(nullable = false)
    private String familyName;
    private String address;
    private String buildingName;
    private String primaryPhoneNumber;
    private String primaryEmail;
    private String nativePlace;
    private String samaj;
    private String primaryBusiness;

    @Enumerated(EnumType.STRING)
    private IncomeRange incomeRange;

    private boolean sadharmik;
    private boolean wantToHelpSadharmik;
    private boolean gruhJinalay;
    private boolean garamPani;
    private boolean chowihar;
    private boolean receiveUpdates;

    private boolean active;

    @OneToMany(mappedBy = "family", fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<Person> persons;

    @OneToMany(mappedBy = "family", fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<Monk> monks;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;
}
