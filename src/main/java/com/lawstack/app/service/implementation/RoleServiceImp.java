package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Role;
import com.lawstack.app.repository.RoleRepository;
import com.lawstack.app.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public Role saveRole(Role role) {

        log.info("Inserting new role in database");
        
        Role result = this.roleRepo.findByRoleName(role.getRoleName());

        if(result==null){
            String id = UUID.randomUUID().toString();
            role.setRoleId(id);
            result = this.roleRepo.save(role);
            return result;
        }

        return null;

    }

    @Override
    public Role getRoleByName(String roleName) {

        log.info("Selecting role from database with given name {}",roleName);
        
        Role result = this.roleRepo.findByRoleName(roleName);

        if(result!=null){
            
            return result;
        }

        return null;
    }

    @Override
    public List<Role> getAllRoles() {
       log.info("Selecting all roles from database");

       List<Role> roles = this.roleRepo.findAll();

       if(roles.isEmpty()){
            return null;
       }
       return roles;
    }

}
