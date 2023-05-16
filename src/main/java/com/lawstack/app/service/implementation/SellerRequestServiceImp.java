package com.lawstack.app.service.implementation;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.model.Seller;
import com.lawstack.app.model.SellerRequest;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.SellerRequestRespository;
import com.lawstack.app.service.SellerRequestService;
import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerRequestServiceImp  implements SellerRequestService{
    
    @Autowired
    private SellerRequestRespository sellerRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    /**
     * @implSpec This will do necessary checks and forward request for processing
     * @since v1.0.0
     * ! May be got some changes in future versions
     */
    @Override
    public SellerRequest requestForSeller(String sellerInfo,MultipartFile document) {
        
        log.info("Saving Seller Request Data in database");

        String id = UUID.randomUUID().toString();
        SellerRequest seller = new SellerRequest();
        ObjectMapper json = new ObjectMapper();

        //* Converting string into JSON
        try {
            seller = json.readValue(sellerInfo, SellerRequest.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }   
        //* Adding profile Picture and Document to Seller JSON */
        try {
            seller.setDocument(document.getBytes());
            seller.setDocumentName(document.getOriginalFilename());
            seller.setDocumentType(document.getContentType());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        //* Saving object into data base */
        
        try {
            seller.setSellerId(id);
            log.info(seller.getUserId());
            seller = this.sellerRepo.save(seller);
        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        return seller;
    }
     /**
     *@implSpec List of all requests made 
     *@since v1.0.0
     *! May be got some changes in future versions
     */
    @Override
    public SellerRequest fetchRequestBySellerId(String sellerId) {
        log.info("Fetch the request by sellerId");

        SellerRequest seller = this.sellerRepo.findById(sellerId).get();

        if(seller==null){
            log.info("Seller not found with given sellerId {}",sellerId);
            return null;
        }
        return seller;
    }
     /**
     *@implSpec List of all requests made 
     *@since v1.0.0
     *! May be got some changes in future versions
     */
    @Override
    public SellerRequest fetchRequestByUserId(String userId) {
        log.info("Fetch the request by userId");

        SellerRequest seller = this.sellerRepo.findByUserId(userId);

        if(seller==null){
            log.info("Seller not found with given userId {}",userId);
            return null;
        }
        return seller;
    }


    /**
     * @implSpec List of all requests made 
     * @since v1.0.0
     * ! May be got some changes in future versions
     */
    @Override
    public List<SellerRequest> getAllRequest() {
        log.info("Fetching all request in inactive state");

        List<SellerRequest> sellersRequest = this.sellerRepo.findAllByisActiveFalse();

        
        if(sellersRequest == null ){
            return null;
        }
        return sellersRequest;
    }

    /**
     * @implSpec Accept the seller request by admin
     * @since v1.0.0
     * ! May be got some changes in future versions
     */
    @Override
    public SellerRequest approvedRequest(String sellerId) {
        
        log.info("Fetcing the seller from database  to for approval");
        SellerRequest seller = this.sellerRepo.findById(sellerId).get();

        if(seller==null){
            log.info("Seller not existed with given sellerId: {}",sellerId);
            return null;
        }
        
        if(seller.isActive()==false){
            
            seller.setActive(true);

            Seller Seller = new Seller();
            User user = new User();

            user.setUserId(seller.getUserId());

            Seller.setUser(user);
            Seller.setActive(false);
            Seller.setEmail(seller.getEmail());
            
            this.sellerService.createSeller(Seller);
            this.userService.updateUserRole(seller.getUserId());
        }
        
        return seller;
    }


}
