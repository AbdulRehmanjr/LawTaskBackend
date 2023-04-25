package com.lawstack.app.service;

import org.springframework.web.multipart.MultipartFile;

import com.lawstack.app.model.Seller;

public interface SellerService {
    
    Seller requestForSeller(String sellerInfo,MultipartFile profilePictre,MultipartFile document ); 
    
}
