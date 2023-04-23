package com.lawstack.app.service.implementation;

import java.util.ArrayList;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Role;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.UserRespository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserRespository userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        User resultUser = this.userRepo.findByEmail(email);


        if(resultUser == null){
            log.error("No user with {} given email ",email);
            return null;
        }
       
 
        return new org.springframework.security.core.userdetails.User(resultUser.getEmail(),resultUser.getPassword(),getAuthorities(resultUser.getRole()));
    }

    private List<GrantedAuthority> getAuthorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
       
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        
        return authorities;
    }
    
}
