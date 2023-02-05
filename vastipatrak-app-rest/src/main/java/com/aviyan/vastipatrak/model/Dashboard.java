package com.aviyan.vastipatrak.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Dashboard {

    private List<Family> allFamilies;

    private List<Person> allPersons;

    private long totalKidsPercentage;//less than 12

    private long totalYouthPercentage;//13 to 50

    private long totalSeniorPercentage;//greater than 50

    private List<Monk> allMonks;

    private List<String> allNativePlaces;

    private List<String> allSamaj;

    private long totalGruhJinalay;

    private long averageIncome;

    private List<Family> wantToHelpSadharmik;

    private Map<String, List<Person>> educationNumbers;

    private Map<String, List<Person>> occupationNumbers;

    @NonNull
    private Group group;

}
