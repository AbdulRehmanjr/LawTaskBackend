package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.CardSubscription;
import com.lawstack.app.model.Seller;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.SellerRepository;
import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.UserService;
import com.lawstack.app.utils.enums.JobNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerServiceImp implements SellerService {

    @Autowired
    private SellerRepository sellerRepo;

    @Autowired
    private UserService userService;

    // private final static String NORMAL ="NORMAL";
    // private final static String EXPERT = "EXPERT";
    // private final static String PRO = "PRO";

    /**
     * @implSpec create a new seller
     * @param Seller seller
     */
    @Override
    public Seller createSeller(Seller seller) {

        log.info("Inserting new seller into database.");

        User user = this.userService.getUserById(seller.getUser().getUserId());

        if (user == null) {
            log.error("Error User Not Found id: {}", seller.getUser().getUserId());
            return null;
        }

        String id = UUID.randomUUID().toString();
        seller.setSellerId(id);
        seller.setUser(user);
       
        
        this.sellerRepo.save(seller);

        return seller;
    }

    @Override
    public Seller getBySellerId(String sellerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBySellerId'");
    }

    @Override
    public List<Seller> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public Seller getSellerByUserId(String userId) {
        log.info("Select seller by its user id");

        Seller seller = this.sellerRepo.findByUserUserId(userId);

        if (seller == null) {
            log.error("Seller not found");
            return null;
        }
        return seller;
    }

    @Override
    public Seller addSubscription(CardSubscription card) {

        Seller seller = this.sellerRepo.findByUserUserId(card.getUserId());
        if(seller ==null){
            log.error("Seller may not exist with given user Id");
            return null;
        }
        seller.setActive(true);
        seller.setMaxJobs(JobNumber.valueOf(card.getSubscription()).getValue());
        seller.setSellerType(card.getSubscription());
        this.sellerRepo.save(seller);
        return seller;
    }

    @Override
    public Seller updateJobStatus(Seller seller) {
       log.info("Update the job status");
       Seller response = this.sellerRepo.findByUserUserId(seller.getUser().getUserId());

       if(response==null){
        return null;
       }
       return this.sellerRepo.save(seller);
    }

    

}
