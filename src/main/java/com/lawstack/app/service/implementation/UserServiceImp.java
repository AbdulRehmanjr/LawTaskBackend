package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Dashboard;
import com.lawstack.app.model.Role;
import com.lawstack.app.model.User;
import com.lawstack.app.model.UserJoin;
import com.lawstack.app.repository.RoleRepository;
import com.lawstack.app.repository.UserRespository;
import com.lawstack.app.service.DashboardService;
import com.lawstack.app.service.EmailService;
import com.lawstack.app.service.SellerAndUserJoinService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    final  String DEFAULT_USER = "USER";
    final  String SELLER = "SELLER";
    final static String ADMIN = "ADMIN";

    @Autowired
    private UserRespository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private SellerAndUserJoinService userJoinService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private EmailService emailService;

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
        user.setPassword(encoder.encode(user.getPassword()));
        User saved = this.userRepo.save(user);
        String message = "Thanks! "+user.getEmail()+" for registration on our website.";
        this.emailService.sendMail(user.getEmail(),"Registration", message);

        this.userJoinService.saveUserJoin(id);
        Dashboard info = new Dashboard();
        info.setUsers(1);
        this.dashboardService.updateDashboard(info);
        
        return saved;

    }
    @Override
    public User saveAdmin(User user) {
        log.info("inserting new admin in database");

        Role role = this.roleRepo.findByRoleName(ADMIN);

        if (role == null) {
            log.info("No role found with given name {}", ADMIN);
            return null;
        }

        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(role);

        User saved =null;
        try {
            saved = this.userRepo.save(user);    
        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
        }
        

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
    /**
     * * update the role of user to seller after admin approval
     */
    @Override
    public User updateUserRole(String userId) {
        
        log.info("Updating the user after its seller request accpeted.");

        User user = this.userRepo.findById(userId).get();

        Role role = this.roleRepo.findByRoleName(SELLER);


        if(user==null){
            log.info("User not found with given user Id");
            return null;
        }

        user.setRole(role);

        this.userRepo.save(user);
        return user;
        

    }

   

}
