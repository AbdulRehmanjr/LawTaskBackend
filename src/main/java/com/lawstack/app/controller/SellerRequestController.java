package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lawstack.app.model.SellerRequest;
import com.lawstack.app.service.SellerRequestService;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sellerrequest")
@CrossOrigin("${cross_origin}")
public class SellerRequestController {

    @Autowired
    private SellerRequestService sellerService;

    /**
     * @apiNote This api will forward request to service layer
     * @param User,picture,document These are recieved as formData
     * @since v1.0.0
     */
    @PostMapping("/request")
    ResponseEntity<?> requestSeller(@RequestParam("seller") String user,
            @RequestParam("document") MultipartFile document) {
        log.info("/POST : Request for seller account");

        log.info("Seller {}",user);
        SellerRequest seller = this.sellerService.requestForSeller(user,document);

        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Error in saving the seller provided data.");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Creation Successfull Now Please wait for admin review on Request");
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<SellerRequest> getSellerByUserId(@PathVariable String userId) {

        log.info("Request for seller using userId: {}", userId);

        SellerRequest seller = this.sellerService.fetchRequestByUserId(userId);

        if (seller != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(seller);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/{sellerId}")
    ResponseEntity<SellerRequest> getSellerById(@PathVariable String sellerId) {

        log.info("Request for seller using sellerId: {}", sellerId);

        SellerRequest seller = this.sellerService.fetchRequestBySellerId(sellerId);

        if (seller != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(seller);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/pending")
    List<SellerRequest> pendingRequests() {

        log.info("/GET :Requesting for pending seller list");

        List<SellerRequest> sellers = this.sellerService.getAllRequest();

        if (sellers.isEmpty()) {
            return null;
        }
        return sellers;
    }
    @GetMapping("/accepted")
    ResponseEntity<List<SellerRequest>> appectedRequests() {

        log.info("Fetching all approved users");

        List<SellerRequest> sellers = this.sellerService.fetchApprovedSellerRequests();

        if (sellers==null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(sellers);
    }
    @PostMapping("/accept/{sellerId}")
    ResponseEntity<?> acceptRequest(@PathVariable String sellerId) {

        log.info("/PUT: to activate the user as a seller");

        SellerRequest seller = this.sellerService.approvedRequest(sellerId);

        if (seller == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(seller);

    }
}
