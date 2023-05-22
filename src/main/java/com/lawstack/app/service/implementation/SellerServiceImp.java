package com.lawstack.app.service.implementation;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.CardSubscription;
import com.lawstack.app.model.Dashboard;

import com.lawstack.app.model.Seller;

import com.lawstack.app.model.User;
import com.lawstack.app.model.UserDashboard;
import com.lawstack.app.repository.SellerRepository;

import com.lawstack.app.service.DashboardService;

import com.lawstack.app.service.SellerAndUserJoinService;

import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.UserDashBoardService;
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

    @Autowired
    private SellerAndUserJoinService sellerJoinService;

    @Autowired
    private UserDashBoardService udashService;

    @Autowired
    private DashboardService dashService;


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

        this.sellerJoinService.saveUserJoin(id);
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
    public Seller addSubscription(CardSubscription card, String email,long amount) {

        Seller seller = this.sellerRepo.findByEmail(email);
        if (seller == null) {
            log.error("Seller may not exist with given email.");
            return null;
        }
        Dashboard dash = new Dashboard();
        UserDashboard udash = new UserDashboard();

        udash.setEmail(email);
        udash.setUserId(seller.getUser().getUserId());
        if (card.getSubscription().equals("Dew Dropper")) {
            log.info("got dew");
            seller.setMaxJobs(JobNumber.valueOf("DEWDROPPER").getValue());
            dash.setDewDropper(1);
            dash.setIncome(amount/100.0);
            
            udash.setSellerType("Dew Dropper");
        } else if (card.getSubscription().equals("Sprinkle Starter")) {
            log.info("got sprinkle");
            seller.setMaxJobs(JobNumber.valueOf("SPRINKLE").getValue());
            dash.setSprinkle(1);
            dash.setIncome(amount/100.0);

            udash.setSellerType("Sprinkle Starter");
        } else if (card.getSubscription().equals("Rain Maker")) {
            log.info("got rain");
            seller.setMaxJobs(JobNumber.valueOf("RAINMAKER").getValue());
            dash.setRainmaker(1);
            dash.setIncome(amount/100.0);

            udash.setSellerType("Rain Maker");
        }

        seller.setActive(true);

        seller.setSellerType(card.getSubscription());
        this.sellerRepo.save(seller);

        this.udashService.saveDashboard(udash);
        this.dashService.updateDashboard(dash);
        return seller;
    }

    @Override
    public Seller updateJobStatus(Seller seller) {
        log.info("Update the job status");
        Seller response = this.sellerRepo.findByUserUserId(seller.getUser().getUserId());

        if (response == null) {
            return null;
        }
        return this.sellerRepo.save(seller);
    }

}
