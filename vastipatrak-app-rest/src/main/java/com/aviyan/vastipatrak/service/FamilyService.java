package com.aviyan.vastipatrak.service;

import com.aviyan.vastipatrak.model.*;
import com.aviyan.vastipatrak.repository.FamilyRepository;
import com.aviyan.vastipatrak.repository.GroupRepository;
import com.aviyan.vastipatrak.repository.MonkRepository;
import com.aviyan.vastipatrak.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FamilyService {

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MonkRepository monkRepository;

    @Autowired
    GroupRepository groupRepository;

    public Family getFamily(Group group, String familyName) {
        Optional<com.aviyan.vastipatrak.entity.Group> optionalGroupEntity =
                groupRepository.findByName(group.getName());
        if(optionalGroupEntity.isPresent()){
            com.aviyan.vastipatrak.entity.Group groupEntity = new com.aviyan.vastipatrak.entity.Group();
            BeanUtils.copyProperties(group, groupEntity);
            Optional<com.aviyan.vastipatrak.entity.Family> familyEntityOptional =
                    familyRepository.findByGroupAndFamilyNameAndActiveTrue(groupEntity, familyName);
            if(familyEntityOptional.isPresent()){
                Family family = new Family();
                BeanUtils.copyProperties(familyEntityOptional.get(), family);

                Group savedGroup = new Group();
                BeanUtils.copyProperties(familyEntityOptional.get(), savedGroup);

                Plan plan = new Plan();
                BeanUtils.copyProperties(familyEntityOptional.get().getGroup().getSubscriptionPlan(), plan);
                savedGroup.setSubscriptionPlan(plan);

                family.setGroup(savedGroup);

                List<Person> persons = new ArrayList<>();
                if(Objects.nonNull(familyEntityOptional.get().getPersons())){
                    familyEntityOptional.get().getPersons().forEach(personEntity -> {
                        Person person = new Person();
                        BeanUtils.copyProperties(personEntity, person);
                        persons.add(person);
                    });
                }
                family.setPersons(persons);

                List<Monk> monks = new ArrayList<>();
                if(Objects.nonNull(familyEntityOptional.get().getMonks())){
                    familyEntityOptional.get().getMonks().forEach(maharajSahebEntity -> {
                        Monk ms = new Monk();
                        BeanUtils.copyProperties(maharajSahebEntity, ms);
                        monks.add(ms);
                    });
                }
                family.setMonks(monks);

                return family;
            }
        }
        return null;
    }

    public List<Family> getFamilies(Group group){
        List<Family> families = new ArrayList<>();

        Optional<com.aviyan.vastipatrak.entity.Group> optionalGroupEntity =
                groupRepository.findById(group.getId());
        optionalGroupEntity.ifPresent(groupEntity ->
                familyRepository.findByGroupAndActiveTrue(groupEntity).forEach(familyEntity -> {
            Family family = new Family();
            BeanUtils.copyProperties(familyEntity, family);

            Group savedGroup = new Group();
            BeanUtils.copyProperties(groupEntity, savedGroup);

            Plan plan = new Plan();
            BeanUtils.copyProperties(groupEntity.getSubscriptionPlan(), plan);
            savedGroup.setSubscriptionPlan(plan);

            family.setGroup(savedGroup);

            List<Person> persons = new ArrayList<>();
            if(Objects.nonNull(familyEntity.getPersons())){
                familyEntity.getPersons().forEach(personEntity -> {
                    Person person = new Person();
                    BeanUtils.copyProperties(personEntity, person);
                    persons.add(person);
                });
            }
            family.setPersons(persons);

            List<Monk> monks = new ArrayList<>();
            if(Objects.nonNull(familyEntity.getMonks())){
                familyEntity.getMonks().forEach(maharajSahebEntity -> {
                    Monk ms = new Monk();
                    BeanUtils.copyProperties(maharajSahebEntity, ms);
                    monks.add(ms);
                });
            }
            family.setMonks(monks);

            families.add(family);
        }));
        return families;
    }

    public boolean canFamilyBeAdded(Group group){
        Optional<com.aviyan.vastipatrak.entity.Group> optionalProprietorEntity =
                groupRepository.findById(group.getId());
        if(optionalProprietorEntity.isPresent()){
            Integer totalFamiliesPresent = familyRepository.countByGroupAndActiveTrue(optionalProprietorEntity.get());
            return optionalProprietorEntity.get().getSubscriptionPlan().getFamilyLimit() > totalFamiliesPresent;
        }
        return false;
    }

    public Family addFamily(String loginId, Family family) {
        Date now = new Date(System.currentTimeMillis());
        com.aviyan.vastipatrak.entity.Family savedFamilyEntity;
        Optional<com.aviyan.vastipatrak.entity.Group> groupEntityOptional =
                groupRepository.findById(family.getGroup().getId());
        if(groupEntityOptional.isPresent()){
            if(familyRepository.findByGroupAndFamilyNameAndActiveTrue(groupEntityOptional.get(), family.getFamilyName()).isPresent()){
                throw new RuntimeException("Family already exists. Please check family name " + family.getFamilyName());
            }

            com.aviyan.vastipatrak.entity.Family familyEntity = new com.aviyan.vastipatrak.entity.Family();
            BeanUtils.copyProperties(family, familyEntity);
            //Set active, create and modified users and time
            familyEntity.setActive(true);
            familyEntity.setCreatedUser(loginId);
            familyEntity.setModifiedUser(loginId);
            familyEntity.setCreatedAt(now);
            familyEntity.setModifiedAt(now);
            familyEntity.setGroup(groupEntityOptional.get());
            savedFamilyEntity = familyRepository.save(familyEntity);

            //Add persons
            List<com.aviyan.vastipatrak.entity.Person> personEntityList = new ArrayList<>();
            if(Objects.nonNull(family.getPersons())){
                family.getPersons().forEach(person -> {
                    com.aviyan.vastipatrak.entity.Person personEntity = new com.aviyan.vastipatrak.entity.Person();
                    BeanUtils.copyProperties(person, personEntity);
                    personEntity.setFamily(savedFamilyEntity);

                    personEntity.setActive(true);
                    personEntity.setCreatedUser(loginId);
                    personEntity.setCreatedAt(now);
                    personEntity.setModifiedUser(loginId);
                    personEntity.setModifiedAt(now);

                    personEntityList.add(personEntity);
                });
            }
            personRepository.saveAll(personEntityList);

            //Add Maharaj saheb
            List<com.aviyan.vastipatrak.entity.Monk> msEntityList = new ArrayList<>();
            if(Objects.nonNull(family.getMonks())){
                family.getMonks().forEach(monk -> {
                    com.aviyan.vastipatrak.entity.Monk monkEntity = new com.aviyan.vastipatrak.entity.Monk();
                    BeanUtils.copyProperties(monk, monkEntity);
                    monkEntity.setFamily(savedFamilyEntity);

                    monkEntity.setActive(true);
                    monkEntity.setCreatedUser(loginId);
                    monkEntity.setCreatedAt(now);
                    monkEntity.setModifiedUser(loginId);
                    monkEntity.setModifiedAt(now);

                    msEntityList.add(monkEntity);
                });
            }
            monkRepository.saveAll(msEntityList);


            Family returnFamily = new Family();
            BeanUtils.copyProperties(savedFamilyEntity, returnFamily);

            List<Person> persons = new ArrayList<>();
            if(Objects.nonNull(savedFamilyEntity.getPersons())){
                savedFamilyEntity.getPersons().forEach(personEntity -> {
                    Person person = new Person();
                    BeanUtils.copyProperties(personEntity, person);
                    persons.add(person);
                });
            }

            List<Monk> monks = new ArrayList<>();
            if(Objects.nonNull(savedFamilyEntity.getMonks())){
                savedFamilyEntity.getMonks().forEach(monkEntity -> {
                    Monk ms = new Monk();
                    BeanUtils.copyProperties(monkEntity, ms);
                    monks.add(ms);
                });
            }

            returnFamily.setMonks(monks);
            returnFamily.setPersons(persons);
            return returnFamily;
        }
        return null;
    }

    public Family updateFamily(String loginId, Family family) {
        com.aviyan.vastipatrak.entity.Family updatedFamilyEntity;
        Optional<com.aviyan.vastipatrak.entity.Group> groupEntityOptional =
                groupRepository.findById(family.getGroup().getId());
        if(groupEntityOptional.isPresent()){

            Optional<com.aviyan.vastipatrak.entity.Family> familyEntityOptional = familyRepository.findById(family.getId());
            if(familyEntityOptional.isPresent()){
                com.aviyan.vastipatrak.entity.Family familyEntity = new com.aviyan.vastipatrak.entity.Family();
                BeanUtils.copyProperties(family, familyEntity);
                //Proprietor can not be changed at the family level
                familyEntity.setGroup(groupEntityOptional.get());
                //This family should be active - UI doesn't return active = true
                familyEntity.setActive(true);
                familyEntity.setCreatedUser(familyEntityOptional.get().getCreatedUser());
                familyEntity.setCreatedAt(familyEntityOptional.get().getCreatedAt());
                familyEntity.setModifiedUser(loginId);
                familyEntity.setModifiedAt(new Date(System.currentTimeMillis()));
                updatedFamilyEntity = familyRepository.save(familyEntity);

                //Add persons
                List<com.aviyan.vastipatrak.entity.Person> personEntityList = new ArrayList<>();
                if(Objects.nonNull(family.getPersons())){
                    family.getPersons().forEach(person -> {
                        //get if the person is existing in db
                        Optional<com.aviyan.vastipatrak.entity.Person> optionalPersonEntity = personRepository.findById(person.getId());
                        com.aviyan.vastipatrak.entity.Person personEntity = new com.aviyan.vastipatrak.entity.Person();
                        if(optionalPersonEntity.isPresent()){
                            personEntity = optionalPersonEntity.get();
                        } else {
                            BeanUtils.copyProperties(person, personEntity);
                            personEntity.setCreatedUser(loginId);
                            personEntity.setCreatedAt(new Date(System.currentTimeMillis()));
                        }
                        personEntity.setActive(true);
                        personEntity.setModifiedUser(loginId);
                        personEntity.setModifiedAt(new Date(System.currentTimeMillis()));

                        personEntity.setFamily(updatedFamilyEntity);
                        personEntityList.add(personEntity);
                    });
                }
                personRepository.saveAll(personEntityList);

                //Add Maharaj saheb
                List<com.aviyan.vastipatrak.entity.Monk> msEntityList = new ArrayList<>();
                if(Objects.nonNull(family.getMonks())){
                    family.getMonks().forEach(maharajSaheb -> {
                        //get if the maharajsaheb is existing in db
                        Optional<com.aviyan.vastipatrak.entity.Monk> optionalMaharajSahebEntity = monkRepository.findById(maharajSaheb.getId());
                        com.aviyan.vastipatrak.entity.Monk maharajSahebEntity = new com.aviyan.vastipatrak.entity.Monk();
                        if(optionalMaharajSahebEntity.isPresent()){
                            maharajSahebEntity = optionalMaharajSahebEntity.get();
                        } else {
                            BeanUtils.copyProperties(maharajSaheb, maharajSahebEntity);
                            maharajSahebEntity.setCreatedUser(loginId);
                            maharajSahebEntity.setCreatedAt(new Date(System.currentTimeMillis()));
                        }
                        maharajSahebEntity.setActive(true);
                        maharajSahebEntity.setModifiedUser(loginId);
                        maharajSahebEntity.setModifiedAt(new Date(System.currentTimeMillis()));

                        maharajSahebEntity.setFamily(updatedFamilyEntity);
                        msEntityList.add(maharajSahebEntity);
                    });
                }
                monkRepository.saveAll(msEntityList);

                Family returnFamily = new Family();
                BeanUtils.copyProperties(updatedFamilyEntity, returnFamily);

                List<Person> persons = new ArrayList<>();
                if(Objects.nonNull(updatedFamilyEntity.getPersons())){
                    updatedFamilyEntity.getPersons().forEach(personEntity -> {
                        Person person = new Person();
                        BeanUtils.copyProperties(personEntity, person);
                        persons.add(person);
                    });
                }
                returnFamily.setPersons(persons);

                List<Monk> monks = new ArrayList<>();
                if(Objects.nonNull(updatedFamilyEntity.getMonks())){
                    updatedFamilyEntity.getMonks().forEach(monkEntity -> {
                        Monk ms = new Monk();
                        BeanUtils.copyProperties(monkEntity, ms);
                        monks.add(ms);
                    });
                }
                return returnFamily;
            }
        }
        return null;
    }

    public boolean disableFamily(String loginId, Family family) {
        Optional<com.aviyan.vastipatrak.entity.Family> existingFamily = familyRepository.findById(family.getId());
        if(existingFamily.isPresent()){
            existingFamily.get().setActive(false);
            existingFamily.get().setModifiedUser(loginId);
            existingFamily.get().setModifiedAt(new Date(System.currentTimeMillis()));
            familyRepository.save(existingFamily.get());

            //Add persons
            List<com.aviyan.vastipatrak.entity.Person> personEntityList = new ArrayList<>();
            if(Objects.nonNull(existingFamily.get().getPersons())){
                existingFamily.get().getPersons().forEach(personEntity -> {
                    personEntity.setActive(false);
                    personEntity.setModifiedUser(loginId);
                    personEntity.setModifiedAt(new Date(System.currentTimeMillis()));
                    personEntityList.add(personEntity);
                });
            }
            personRepository.saveAll(personEntityList);

            return true;
        } else {
            return false;
        }
    }
}
