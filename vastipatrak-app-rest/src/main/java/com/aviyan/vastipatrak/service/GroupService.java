package com.aviyan.vastipatrak.service;

import com.aviyan.vastipatrak.model.Group;
import com.aviyan.vastipatrak.model.Role;
import com.aviyan.vastipatrak.repository.GroupRepository;
import com.aviyan.vastipatrak.repository.LoginRepository;
import com.aviyan.vastipatrak.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    LoginService loginService;

    public boolean isProprietorExist(String name) {
        return groupRepository.findByName(name).isPresent();
    }

    public Group getGroup(String name) {
        Optional<com.aviyan.vastipatrak.entity.Group> groupEntityOptional = groupRepository.findByName(name);
        if(groupEntityOptional.isPresent()){
            Group group = new Group();
            BeanUtils.copyProperties(groupEntityOptional.get(), group);
            return group;
        }

        return null;
    }

    public Role addProprietor(String loginId, Role role) {
        //Get all roles associate with login to add new proprietor to it
        Optional<com.aviyan.vastipatrak.entity.Login> optionalLoginEntity = loginRepository.findByLoginId(loginId);
        optionalLoginEntity.ifPresent(login -> loginService.addRolesWithProprietor(Collections.singletonList(role), login));

        return loginService.getLogin(loginId).getRoles().stream()
                .filter(newlyAddedRole -> newlyAddedRole.getGroup().getName().equals(role.getGroup().getName())).findFirst().orElse(null);
    }

    public Group updateProprietor(String loginId, Group group) {
        com.aviyan.vastipatrak.entity.Group updatedProprietorEntity;
        Optional<com.aviyan.vastipatrak.entity.Group> proprietorEntityOptional = groupRepository.findById(group.getId());
        if(proprietorEntityOptional.isPresent()){
            com.aviyan.vastipatrak.entity.Group proprietorEntityToUpdate = new com.aviyan.vastipatrak.entity.Group();
            BeanUtils.copyProperties(group, proprietorEntityToUpdate);
            //Set proprietor active
            proprietorEntityToUpdate.setActive(true);

            com.aviyan.vastipatrak.entity.Plan planEntity = new com.aviyan.vastipatrak.entity.Plan();
            BeanUtils.copyProperties(proprietorEntityOptional.get().getSubscriptionPlan(), planEntity);
            proprietorEntityToUpdate.setSubscriptionPlan(planEntity);

            proprietorEntityToUpdate.setCreatedUser(proprietorEntityOptional.get().getCreatedUser());
            proprietorEntityToUpdate.setCreatedAt(proprietorEntityOptional.get().getCreatedAt());
            proprietorEntityToUpdate.setModifiedUser(loginId);
            proprietorEntityToUpdate.setModifiedAt(new Date(System.currentTimeMillis()));
            updatedProprietorEntity = groupRepository.save(proprietorEntityToUpdate);

            Group returnProprietor = new Group();
            BeanUtils.copyProperties(updatedProprietorEntity, returnProprietor);
            return returnProprietor;
        }
        return null;
    }

    public boolean disableGroup(String loginId, Group group) {
        Optional<com.aviyan.vastipatrak.entity.Group> existingProprietor = groupRepository.findById(group.getId());
        if(existingProprietor.isPresent()){
            existingProprietor.get().setActive(false);
            existingProprietor.get().setModifiedUser(loginId);
            existingProprietor.get().setModifiedAt(new Date(System.currentTimeMillis()));
            groupRepository.save(existingProprietor.get());
            return true;
        } else {
            return false;
        }
    }
}
