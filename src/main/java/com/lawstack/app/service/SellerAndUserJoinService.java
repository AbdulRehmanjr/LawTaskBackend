package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.SellerJoin;
import com.lawstack.app.model.UserJoin;

public interface SellerAndUserJoinService {
    

    void saveUserJoin(String id);

    void saveSellerJoin(String id);

    List<UserJoin> getAllUsers();

    List<SellerJoin> getAllSellers();
}
