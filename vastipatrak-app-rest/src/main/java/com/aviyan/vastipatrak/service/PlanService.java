package com.aviyan.vastipatrak.service;

import com.aviyan.vastipatrak.model.Plan;
import com.aviyan.vastipatrak.repository.PlanRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanService {

    @Autowired
    PlanRepository planRepository;

    public List<Plan> getPlans(){
        List<Plan> plans = new ArrayList<>();
        List<com.aviyan.vastipatrak.entity.Plan> plansEntity = planRepository.findAll();
        plansEntity.forEach(planEntity -> {
            Plan plan = new Plan();
            BeanUtils.copyProperties(planEntity, plan);
            plans.add(plan);
        });
        return plans;
    }

    public Plan getPlan(String name){
        Plan plan = new Plan();
        com.aviyan.vastipatrak.entity.Plan planEntity = planRepository.findByName(name);
        BeanUtils.copyProperties(planEntity, plan);
        return plan;
    }

    public void addPlan(Plan plan){
        com.aviyan.vastipatrak.entity.Plan planEntity = new com.aviyan.vastipatrak.entity.Plan();
        BeanUtils.copyProperties(plan, planEntity);
        planRepository.save(planEntity);
    }
}
