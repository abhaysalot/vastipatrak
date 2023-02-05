package com.aviyan.vastipatrak.repository;

import com.aviyan.vastipatrak.entity.Family;
import com.aviyan.vastipatrak.entity.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends CrudRepository<Family, Long> {

    Optional<Family> findByGroupAndFamilyNameAndActiveTrue(Group group, String familyName);

    List<Family> findByGroupAndActiveTrue(Group group);

    int countByGroupAndActiveTrue(Group group);

}
