package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.User;

public interface UserService {
    
     User saveUser(User user);

     User saveAdmin(User user);

     List<User> getAllUsersByUserName(String userName);

     User getUserById(String userId);

     User getUserByIdEdit(String userId);

     User updateUser(User user);
     
     List<User> getAllUsersByUserNameLike(String userNameLike);
    
     List<User> getAllUsers();
     
     User getUserByEmail(String email);

     void deleteUser(String id);

     void restPassword(int otp,String email);

     User updatePassword(User user);

     User updateUserRole(String userId);
}