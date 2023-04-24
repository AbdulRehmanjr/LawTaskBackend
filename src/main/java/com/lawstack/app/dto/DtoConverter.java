package com.lawstack.app.dto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lawstack.app.model.User;


public class DtoConverter {
    
    
    public UserDto UserToDto(User user){

       return  new UserDto(user.getUserId(), user.getUserName(), user.getEmail(), user.getProfilePicture(), user.getRole());
    }
}
