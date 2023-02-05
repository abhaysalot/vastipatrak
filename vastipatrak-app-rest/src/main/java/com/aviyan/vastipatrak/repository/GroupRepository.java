package com.aviyan.vastipatrak.repository;

import com.aviyan.vastipatrak.entity.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

    Optional<Group> findByName(String name);

}
