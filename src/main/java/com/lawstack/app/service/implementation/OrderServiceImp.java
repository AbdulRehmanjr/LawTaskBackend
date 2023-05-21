package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Job;
import com.lawstack.app.model.Order;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.OrderRepository;
import com.lawstack.app.service.EmailService;
import com.lawstack.app.service.JobService;
import com.lawstack.app.service.OrderService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private EmailService emailService;
    
    @Override
    public Order saveOrder(Order order) {
       
        log.info("Saving the New Order");

        String id = UUID.randomUUID().toString();
      
        User user = this.userService.getUserById(order.getUser().getUserId());
        Job job = this.jobService.getByJobId(order.getJob().getJobId());

      
        user.setUserId(user.getUserId());
        user.setEmail(user.getEmail());
        user.setUserName(user.getUserName());
        user.setProfilePicture(null);

     
        job.setJobId(job.getJobId());
        job.setJobName(job.getJobName());
        job.setJobImage(null);
        Order request = new Order();

        request.setId(id);
        request.setDescription(order.getDescription());
        request.setStartedDate(order.getStartedDate());
        request.setEndedDate(order.getEndedDate());
        request.setJob(job);
        request.setPrice(order.getPrice());
        request.setRequirementFile(order.getRequirementFile());
        request.setUser(user);
        request.setConfirmed(true);

        Order response = this.orderRepository.save(request);

      
        return response;
    }

    @Override
    public Order getOrderById(String orderId) {
        
        log.info("Geting order by id: {}",orderId);

        return this.orderRepository.findById(orderId).get();

    }

    @Override
    public List<Order> getAllOrders() {
       
        log.info("Geting all orders");

        return this.orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersByUserId(String userId) {
        
        log.info("geting all order by userId");

        return this.orderRepository.findAllByUserUserId(userId);
    }   

    @Override
    public void deleteOrder(String id) {
        
        Order order = this.getOrderById(id);

        if(order != null){
            this.orderRepository.delete(order);
        }
    }

    @Override
    @Deprecated
    public Order updateOrder(Order order) {

        Order response = this.orderRepository.findByIdAndConfirmedFalse(order.getId());
        
        if(response!=null){

            response.setCustomerEmail(order.getCustomerEmail());
            response.setCustomerName(order.getCustomerName());
            response.setRequirementFile(order.getRequirementFile());
            response.setCustomerId(order.getCustomerId());
            response = this.orderRepository.save(response);
            String message ="""
                    Customer made an Order.Please check it.
                    Email: %s
                    User Name: %s
                    Visite your dashboard for more details.
                    """.formatted(order.getCustomerEmail(),order.getCustomerName());
                    try {
                        this.emailService.sendMail(order.getCustomerEmail(), "Order Received" , message);
                    } catch (Exception e) {
                        log.info("Error {} ",e.getMessage());
                    }
            

            return response;
        }

        return null;
    }

    @Override
    public List<Order> getAllOrdersByCustomerEmail(String email) {
       log.info("Getting all Orders By Email");

       return this.orderRepository.findAllByCustomerEmail(email);
    }
    

}
