package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.CardSubscription;
import com.lawstack.app.model.Seller;

public interface SellerService {

    Seller createSeller(Seller seller);

    Seller getBySellerId(String sellerId);

    List<Seller> getAll();

    Seller getByEmail(String email);

    Seller getSellerByUserId(String userId);

    Seller addSubscription(CardSubscription card,String email,long amount);

    Seller updateJobStatus(Seller seller);
}
