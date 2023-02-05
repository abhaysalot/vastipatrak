package com.aviyan.vastipatrak.service;

import com.aviyan.vastipatrak.model.Dashboard;
import com.aviyan.vastipatrak.model.Family;
import com.aviyan.vastipatrak.model.Group;
import com.aviyan.vastipatrak.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    FamilyService familyService;

    public Dashboard getDashboard(Group group){
        Optional<com.aviyan.vastipatrak.entity.Group> optionalGroupEntity = groupRepository.findById(group.getId());
        if(optionalGroupEntity.isPresent()){
            Dashboard dashboard = new Dashboard();

            List<Family> allFamilies = familyService.getFamilies(group);

            dashboard.setAllFamilies(allFamilies);
            dashboard.setAllPersons(allFamilies.stream().flatMap(family -> family.getPersons().stream()).collect(Collectors.toList()));
            dashboard.setAllMonks(allFamilies.stream().flatMap(family -> family.getMonks().stream()).collect(Collectors.toList()));

            dashboard.setGroup(group);
            return dashboard;
        }
        return null;
    }

}
