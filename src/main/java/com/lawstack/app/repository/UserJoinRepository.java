package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.UserJoin;

public interface UserJoinRepository extends JpaRepository<UserJoin,Integer>{
    
}
