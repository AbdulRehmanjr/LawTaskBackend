package com.lawstack.app.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.SellerJoin;
import com.lawstack.app.model.UserJoin;
import com.lawstack.app.repository.SellerJoinRepository;
import com.lawstack.app.repository.UserJoinRepository;
import com.lawstack.app.service.SellerAndUserJoinService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SellerAndUserJoinServiceImp implements SellerAndUserJoinService{
    
    
    @Autowired
    private SellerJoinRepository sjRepo;

    @Autowired
    private UserJoinRepository ujRepo;
    @Override
    public void saveUserJoin(String id) {
    
        log.info("Saving the user join history");

        UserJoin userJoin = new UserJoin();

        userJoin.setUserId(id);

        this.ujRepo.save(userJoin);

    }

    @Override
    public void saveSellerJoin(String id) {
       log.info("Saving the seller join history");

       SellerJoin sellerJoin = new SellerJoin();

       sellerJoin.setSellerId(id);

       this.sjRepo.save(sellerJoin);
    }

    @Override
    public List<UserJoin> getAllUsers() {
        log.info("Getting all info of user join history");

        return this.ujRepo.findAll();
    }

    @Override
    public List<SellerJoin> getAllSellers() {
        
        log.info("Getting all info of seller join history");

        return this.sjRepo.findAll();
    }
    
}
