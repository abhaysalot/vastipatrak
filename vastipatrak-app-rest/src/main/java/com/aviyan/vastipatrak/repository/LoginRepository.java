package com.aviyan.vastipatrak.repository;

import com.aviyan.vastipatrak.entity.Login;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends CrudRepository<Login, Long> {

    Optional<Login> findByLoginId(String loginId);

}
