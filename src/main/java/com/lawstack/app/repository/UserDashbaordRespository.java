package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.UserDashboard;

public interface UserDashbaordRespository extends JpaRepository<UserDashboard,String>{

    UserDashboard findByEmail(String email);
    
    UserDashboard findByUserId(String id);
}
