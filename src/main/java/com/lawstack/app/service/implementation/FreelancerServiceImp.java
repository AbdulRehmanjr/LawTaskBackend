package com.lawstack.app.service.implementation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Freelancer;
import com.lawstack.app.model.Order;
import com.lawstack.app.repository.FreeLancerRepository;
import com.lawstack.app.service.FreelancerService;
import com.lawstack.app.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FreelancerServiceImp implements FreelancerService {

    @Autowired
    private FreeLancerRepository flRepo;

    @Autowired
    private OrderService orderService;

    @Override
    public Freelancer saveFreelancer(Freelancer freelancer) {

        log.info("Saving the freelancer in database");

        String id = UUID.randomUUID().toString();

        freelancer.setId(id);

        return this.flRepo.save(freelancer);
    }

    @Override
    public Freelancer getOneFreelancer(String id) {

        log.info("Fetching the Freelancer by id : {}", id);
        Freelancer freelancer = this.flRepo.findById(id).get();
        if (freelancer == null) {
            return null;
        }
        return freelancer;
    }

    @Override
    public List<Freelancer> getTopRatedFreelancers() {
        log.info("Fetching all freelacners");

        List<Freelancer> freelancers = this.flRepo.findAll();

        if (freelancers != null) {
            freelancers = freelancers.stream()
                    .sorted(Comparator.comparing(Freelancer::getRating))
                    .limit(10)
                    .collect(Collectors.toList());
            return freelancers;
        }

        return null;
    }

    @Override
    public List<Freelancer> getAllFreelancers() {
        log.info("Fetching all freelacners");

        return this.flRepo.findAll();
    }

    @Override
    public Freelancer updateRating(String id, int rating, String comment, String orderId) {

        Freelancer freelancer = this.flRepo.findBySellerUserUserId(id);

        Order order = this.orderService.getOrderById(orderId);

        if (freelancer != null && order != null) {
            int value = 100;
            String end = order.getEndedDate();

            LocalDate endDate = LocalDate.parse(end);

            LocalDate currentDate = LocalDate.now();

            long diffDays = ChronoUnit.DAYS.between(currentDate, endDate);
           
            if (diffDays < 0) {
                if (diffDays <= -5) {
                    
                    value = value + ((int)diffDays*2);
                   
                }
            }
            int updatedRating = (freelancer.getRating() + rating) / 2;
            int success = (freelancer.getSuccess() + value) / 2;

            freelancer.setRating(updatedRating);
            freelancer.setSuccess(success);

            if (freelancer.getComments() == null) {

                List<String> comments = new ArrayList<>();
                comments.add(comment);
                freelancer.setComments(comments);
            } else {

                freelancer.getComments().add(comment);
            }

            freelancer = this.flRepo.save(freelancer);

            return freelancer;
        }
        log.error("Error in updating freelancer ratint and comments");
        return null;
    }

    @Override
    public Freelancer getFreelancerByUserId(String id) {

        log.info("Fetching the Freelancer by user id : {}", id);

        Freelancer freelancer = this.flRepo.findBySellerUserUserId(id);

        if (freelancer == null) {
            return null;
        }
        return freelancer;
    }

}
