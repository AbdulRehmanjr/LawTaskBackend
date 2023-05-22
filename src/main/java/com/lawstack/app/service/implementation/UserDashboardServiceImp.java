package com.lawstack.app.service.implementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.UserDashboard;
import com.lawstack.app.repository.UserDashbaordRespository;
import com.lawstack.app.service.UserDashBoardService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDashboardServiceImp implements UserDashBoardService {

    @Autowired
    private UserDashbaordRespository uDashRepo;

    @Override
    public UserDashboard saveDashboard(UserDashboard dashboard) {
       log.info("Saving user dashboard");
        String id = UUID.randomUUID().toString();

        dashboard.setId(id);

        return this.uDashRepo.save(dashboard);
    }

    @Override
    public UserDashboard getuserInfo(String email) {
        log.info("Get user dashboard info by email: {}",email);
        return this.uDashRepo.findByEmail(email);
    }

    @Override
    public UserDashboard getInfoByUserId(String id) {
        log.info("Get the user dashboard by userId {}",id);

        return this.uDashRepo.findByUserId(id);
    }

    @Override
    public UserDashboard updateDashboard(UserDashboard userDashboard) {
        log.info("updating the dashboard");
        UserDashboard  dash = this.getInfoByUserId(userDashboard.getUserId());


        if(userDashboard.getJobs()!=0){
            dash.setJobs(dash.getJobs()+1);
        }
        if(userDashboard.getRevenue()!=0){
            dash.setRevenue(userDashboard.getRevenue());
        }
        if(userDashboard.getSellerType()!=null){
            dash.setSellerType(userDashboard.getSellerType());
        }
        return this.uDashRepo.save(dash);
    }



}
