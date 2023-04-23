package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Role;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.RoleRepository;
import com.lawstack.app.repository.UserRespository;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    final static String DEFAULT_USER = "USER";

    @Autowired
    private UserRespository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public User saveUser(User user) {

        log.info("inserting new user in database");

        Role role = this.roleRepo.findByRoleName(DEFAULT_USER);

        if (role == null) {
            log.info("No role found with given name {}", DEFAULT_USER);
            return null;
        }

        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        user.setRole(role);

        User saved = this.userRepo.save(user);

        return saved;

    }

    @Override
    public User getUserById(String userId) {

        log.info("fetching user with id {}", userId);

        return this.userRepo.findById(userId).get();
    }

    @Override
    public List<User> getAllUsersByUserNameLike(String userNameLike) {

        log.info("All users with given pattern in them.");

        return this.userRepo.findByUserNameContains(userNameLike);
    }

    @Override
    public List<User> getAllUsersByUserName(String userName) {

        log.info("All users with exact given name: {}", userName);

        List<User> users = this.userRepo.findByUserName(userName);

        if (users == null) {
            log.info("No user found with given user name");
            return null;
        }
        return users;
    }

    @Override
    public void deleteUser(String id) {
        log.info("DELETE with id :{}", id);
        this.userRepo.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return this.userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Getting By email");
        return this.userRepo.findByEmail(email);
    }

}
