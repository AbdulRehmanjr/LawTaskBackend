package com.lawstack.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lawstack.app.model.Seller;

public interface SellerService {
    
    Seller requestForSeller(String sellerInfo,MultipartFile profilePictre,MultipartFile document ); 
    
    Seller fetchRequestByUserId(String userId);
    
    List<Seller> getAllRequest();
}
