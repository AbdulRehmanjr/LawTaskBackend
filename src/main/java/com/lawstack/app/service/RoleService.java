package com.lawstack.app.service;



import java.util.List;

import com.lawstack.app.model.Role;

public interface RoleService {
    
    Role saveRole(Role role);

    Role getRoleByName(String roleName);

    List<Role> getAllRoles();
}
