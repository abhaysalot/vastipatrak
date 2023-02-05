package com.aviyan.vastipatrak.service;

import com.aviyan.vastipatrak.model.Group;
import com.aviyan.vastipatrak.model.Login;
import com.aviyan.vastipatrak.model.Role;
import com.aviyan.vastipatrak.repository.GroupRepository;
import com.aviyan.vastipatrak.repository.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    GroupRepository groupRepository;

    public List<Role> getRoles(Login login){
        List<Role> roles = new ArrayList<>();
        com.aviyan.vastipatrak.entity.Login loginEntity = new com.aviyan.vastipatrak.entity.Login();
        BeanUtils.copyProperties(login, loginEntity);
        List<com.aviyan.vastipatrak.entity.Role> rolesEntity = roleRepository.findByLogin(loginEntity);
        rolesEntity.forEach(roleEntity -> {
            Role role = new Role();
            BeanUtils.copyProperties(roleEntity, role);
            roles.add(role);
        });
        return roles;
    }

    public List<Role> getRoles(Group group){
        List<Role> roles = new ArrayList<>();
        Optional<com.aviyan.vastipatrak.entity.Group> groupEntityOptional = groupRepository.findById(group.getId());
        if(groupEntityOptional.isPresent()){
            List<com.aviyan.vastipatrak.entity.Role> rolesEntity = roleRepository.findByGroup(groupEntityOptional.get());
            rolesEntity.forEach(roleEntity -> {
                Role role = new Role();
                BeanUtils.copyProperties(roleEntity, role);
                roles.add(role);
            });
        }
        return roles;
    }

    public Role updateRole(String loginId, Role role) {
        Date now = new Date(System.currentTimeMillis());
        com.aviyan.vastipatrak.entity.Login loginEntity = new com.aviyan.vastipatrak.entity.Login();
        BeanUtils.copyProperties(role.getLogin(), loginEntity);

        com.aviyan.vastipatrak.entity.Group groupEntity = new com.aviyan.vastipatrak.entity.Group();
        BeanUtils.copyProperties(role.getGroup(), groupEntity);

        com.aviyan.vastipatrak.entity.Role roleEntity = new com.aviyan.vastipatrak.entity.Role();
        Optional<com.aviyan.vastipatrak.entity.Role> roleEntityOptional = roleRepository.findByLoginAndGroup(loginEntity, groupEntity);
        if(roleEntityOptional.isPresent()){
            roleEntity = roleEntityOptional.get();
            roleEntity.setCreatedUser(roleEntityOptional.get().getCreatedUser());
            roleEntity.setCreatedAt(roleEntityOptional.get().getCreatedAt());
        } else {
            BeanUtils.copyProperties(role, roleEntity);
            roleEntity.setCreatedUser(loginId);
            roleEntity.setCreatedAt(now);
        }

        roleEntity.setLogin(loginEntity);
        roleEntity.setGroup(groupEntity);
        roleEntity.setModifiedUser(loginId);
        roleEntity.setModifiedAt(now);
        com.aviyan.vastipatrak.entity.Role savedRole = roleRepository.save(roleEntity);

        Role returnRole = new Role();
        BeanUtils.copyProperties(savedRole, returnRole);
        return returnRole;
    }
}
