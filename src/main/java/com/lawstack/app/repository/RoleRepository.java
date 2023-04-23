package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Role;

public interface RoleRepository  extends JpaRepository<Role,String>{
    Role findByRoleName(String role);    
}
