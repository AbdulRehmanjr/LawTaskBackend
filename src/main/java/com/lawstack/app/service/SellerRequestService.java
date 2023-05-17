package com.lawstack.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lawstack.app.model.SellerRequest;

public interface SellerRequestService {
    
    SellerRequest requestForSeller(String sellerInfo,MultipartFile document ); 
    
    SellerRequest fetchRequestBySellerId(String sellerId);
    
    List<SellerRequest> fetchApprovedSellerRequests();
    SellerRequest fetchRequestByUserId(String userId);
    
    List<SellerRequest> getAllRequest();

    SellerRequest approvedRequest(String userId);
}
