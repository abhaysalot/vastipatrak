package com.aviyan.vastipatrak.repository;

import com.aviyan.vastipatrak.entity.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {

    List<Plan> findAll();

    Plan findByName(String name);
}
