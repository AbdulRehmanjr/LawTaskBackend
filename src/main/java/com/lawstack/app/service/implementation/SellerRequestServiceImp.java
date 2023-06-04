package com.lawstack.app.service.implementation;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.model.Dashboard;
import com.lawstack.app.model.Freelancer;
import com.lawstack.app.model.Notification;
import com.lawstack.app.model.Seller;
import com.lawstack.app.model.SellerRequest;
import com.lawstack.app.model.User;
import com.lawstack.app.model.UserDashboard;
import com.lawstack.app.repository.SellerRequestRespository;
import com.lawstack.app.service.DashboardService;
import com.lawstack.app.service.EmailService;
import com.lawstack.app.service.FreelancerService;
import com.lawstack.app.service.NotificationService;
import com.lawstack.app.service.SellerAndUserJoinService;
import com.lawstack.app.service.SellerRequestService;
import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerRequestServiceImp implements SellerRequestService {

    @Autowired
    private SellerRequestRespository sellerRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private FreelancerService freelancerService;


    @Autowired
    private EmailService emailService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SellerAndUserJoinService sellerJoinService;

    @Autowired
    private NotificationService notService;

    /**
     * @implSpec This will do necessary checks and forward request for processing
     * @since v1.0.0
     *        ! May be got some changes in future versions
     */
    @Override
    public SellerRequest requestForSeller(String sellerInfo, MultipartFile document) {

        log.info("Saving Seller Request Data in database");

        String id = UUID.randomUUID().toString();
        SellerRequest seller = new SellerRequest();
        ObjectMapper json = new ObjectMapper();

        // * Converting string into JSON
        try {
            seller = json.readValue(sellerInfo, SellerRequest.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        // * Adding profile Picture and Document to Seller JSON */
        try {
            seller.setDocument(document.getBytes());
            seller.setDocumentName(document.getOriginalFilename());
            seller.setDocumentType(document.getContentType());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        // * Saving object into data base */

        try {
            seller.setSellerId(id);

            User user = this.userService.getUserById(seller.getUser().getUserId());

            seller.setUser(user);

            seller = this.sellerRepo.save(seller);
            
            Notification notification = new Notification();
            String content = """
                    %s Joined Our Platform.
                    """.formatted(user.getUserName());
    
            notification.setContent(content);
            notification.setUserId(user.getUserId());
            
            this.notService.saveNotification(notification);
            
            this.emailService.sendMail(user.getEmail(), "To Become a Seller",
                    "Your Request to become Seller has been forwrded to admin please wait for their response.");

        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        return seller;
    }

    /**
     * @implSpec List of all requests made
     * @since v1.0.0
     *        ! May be got some changes in future versions
     */
    @Override
    public SellerRequest fetchRequestBySellerId(String sellerId) {
        log.info("Fetch the request by sellerId");

        SellerRequest seller = this.sellerRepo.findById(sellerId).get();

        if (seller == null) {
            log.info("Seller not found with given sellerId {}", sellerId);
            return null;
        }
        return seller;
    }

    /**
     * @implSpec List of all requests made
     * @since v1.0.0
     *        ! May be got some changes in future versions
     */
    @Override
    public SellerRequest fetchRequestByUserId(String userId) {
        log.info("Fetch the request by userId");

        SellerRequest seller = this.sellerRepo.findByUserUserId(userId);

        if (seller == null) {
            log.info("Seller not found with given userId {}", userId);
            return null;
        }
        return seller;
    }

    /**
     * @implSpec List of all requests made
     * @since v1.0.0
     *        ! May be got some changes in future versions
     */
    @Override
    public List<SellerRequest> getAllRequest() {
        log.info("Fetching all request in inactive state");

        List<SellerRequest> sellersRequest = this.sellerRepo.findAllByisActiveFalse();

        if (sellersRequest == null) {
            return null;
        }
        return sellersRequest;
    }

    /**
     * @implSpec Accept the seller request by admin
     * @since v1.0.0
     *        ! May be got some changes in future versions
     */
    @Override
    public SellerRequest approvedRequest(String sellerId) {

        log.info("Fetcing the seller from database  to for approval");

        SellerRequest seller = this.sellerRepo.findById(sellerId).get();

        if (seller == null) {
            log.info("Seller not existed with given sellerId: {}", sellerId);
            return null;
        }

        if (seller.isActive() == false) {

            seller.setActive(true);

            Seller Seller = new Seller();
            User user = this.userService.getUserById(seller.getUser().getUserId());

            Seller.setUser(user);
            Seller.setActive(false);
            Seller.setEmail(seller.getEmail());

            this.sellerService.createSeller(Seller);
            this.userService.updateUserRole(user.getUserId());

            Freelancer freelancer = new Freelancer();

            freelancer.setSeller(seller);

            this.freelancerService.saveFreelancer(freelancer);

            UserDashboard udash = new UserDashboard();

            udash.setEmail(user.getEmail());
            udash.setJobs(0);
            Dashboard dash = new Dashboard();

            dash.setSellers(1);
            this.dashboardService.updateDashboard(dash);

            this.sellerJoinService.saveSellerJoin(seller.getUser().getUserId());

            
            Notification notification = new Notification();
            String content = """
                    %s Your Request has been approved by the Administration.
                    """.formatted(user.getUserName());
    
            notification.setContent(content);
            notification.setUserId(seller.getUser().getUserId());
            
            this.notService.saveNotification(notification);

            this.emailService.sendMail(Seller.getEmail(), "Seller Request Approval",
                    "Your Request has been approved by the Administration.");
        }

        return seller;
    }

    @Override
    public List<SellerRequest> fetchApprovedSellerRequests() {
        log.info("Fetching all the approved Requests");
        return this.sellerRepo.findAllByisActiveTrue();
    }

    @Override
    public SellerRequest rejectRequest(String userId, String remarks) {
        log.info("Fetcing the seller from database for rejection.");
        SellerRequest seller = this.sellerRepo.findById(userId).get();

        if (seller == null) {
            log.info("Seller not existed with given sellerId: {}", userId);
            return null;
        }

        if (seller.isActive() == false) {

            seller.setRejected(true);
            seller.setRemarks(remarks);

            User user = this.userService.getUserById(seller.getUser().getUserId());

            this.sellerRepo.save(seller);
            
            Notification notification = new Notification();
            String content = """
                    %s Your Request has been rejected by the Administration.
                    """.formatted(user.getUserName());
    
            notification.setContent(content);
            notification.setUserId(user.getUserId());
            
            this.notService.saveNotification(notification);

            this.emailService.sendMail(user.getEmail(), "Seller Request Approval",
                    "Your Request has been rejected by the Administration.");
        }

        return seller;
    }

    @Override
    public SellerRequest updateSeller(String sellerInfo, MultipartFile document) {
        log.info("Updating Seller Request Data in database");

        SellerRequest seller = new SellerRequest();
        ObjectMapper json = new ObjectMapper();

        // * Converting string into JSON
        try {
            seller = json.readValue(sellerInfo, SellerRequest.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        // * Adding profile Picture and Document to Seller JSON */
        try {
            seller.setDocument(document.getBytes());
            seller.setDocumentName(document.getOriginalFilename());
            seller.setDocumentType(document.getContentType());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        // * Saving object into data base */

        try {

            seller.setRejected(false);
            seller.setRemarks("");
            seller = this.sellerRepo.save(seller);
            this.emailService.sendMail(seller.getUser().getEmail(), "To Become a Seller",
                    "Your Request to become Seller has been forwrded to admin please wait for their response.");

        } catch (Exception e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        return seller;
    }

}
