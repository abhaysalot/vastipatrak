package com.aviyan.vastipatrak.model;

import com.aviyan.vastipatrak.constant.IncomeRange;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Family {

    private long id;

    @NonNull
    private String familyName;

    @NonNull
    private String primaryPhoneNumber;
    @NonNull
    private String primaryEmail;
    @NonNull
    private String address;

    private String buildingName;
    private String nativePlace;
    private String samaj;

    private String primaryBusiness;
    private IncomeRange incomeRange;

    private boolean sadharmik;
    private boolean wantToHelpSadharmik;
    private boolean gruhJinalay;
    private boolean garamPani;
    private boolean chowihar;
    private boolean receiveUpdates;

    @NonNull
    private boolean active;

    private List<Person> persons = new ArrayList<>();
    private List<Monk> monks = new ArrayList<>();

    private Group group;
}
