package com.aviyan.vastipatrak.repository;

import com.aviyan.vastipatrak.entity.Monk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonkRepository extends CrudRepository<Monk, Long> {

}
