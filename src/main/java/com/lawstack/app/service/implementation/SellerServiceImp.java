package com.lawstack.app.service.implementation;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.model.Seller;
import com.lawstack.app.repository.SellerRespository;
import com.lawstack.app.service.SellerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerServiceImp  implements SellerService{
    
    @Autowired
    private SellerRespository sellerRepo;


    
    /**
     * @implSpec This will do necessary checks and forward request for processing
     * @since v1.0.0
     * ! May be got some changes in future versions
     */
    @Override
    public Seller requestForSeller(String sellerInfo, MultipartFile profilePictre, MultipartFile document) {
        
        log.info("Saving Seller Request Data in database");

        String id = UUID.randomUUID().toString();
        Seller seller = new Seller();
        ObjectMapper json = new ObjectMapper();

        //* Converting string into JSON
        try {
            seller = json.readValue(sellerInfo, Seller.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }   
        //* Adding profile Picture and Document to Seller JSON */
        try {
            seller.setProfilePicture(profilePictre.getBytes());
            seller.setDocument(document.getBytes());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        //* Saving object into data base */
        
        try {
            seller.setSellerId(id);
            seller = this.sellerRepo.save(seller);
        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        return seller;
    }


}
