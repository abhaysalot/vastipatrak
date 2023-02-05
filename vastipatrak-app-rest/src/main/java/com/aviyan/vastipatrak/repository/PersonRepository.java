package com.aviyan.vastipatrak.repository;

import com.aviyan.vastipatrak.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

}
