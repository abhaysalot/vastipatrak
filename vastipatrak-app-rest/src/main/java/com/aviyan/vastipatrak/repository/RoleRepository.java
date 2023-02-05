package com.aviyan.vastipatrak.repository;

import com.aviyan.vastipatrak.entity.Login;
import com.aviyan.vastipatrak.entity.Group;
import com.aviyan.vastipatrak.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, CrudRepository<Role, Long> {

    List<Role> findByLogin(Login login);

    List<Role> findByGroup(Group group);

    Optional<Role> findByLoginAndGroup(Login login, Group group);

}
