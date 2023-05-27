package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Job;
import com.lawstack.app.model.Order;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.OrderRepository;
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


    
    @Override
    public Order saveOrder(Order order) {
       
        log.info("Saving the New Order");

        String id = UUID.randomUUID().toString();
      
        User existedUser = this.userService.getUserById(order.getUser().getUserId());
        Job existedJob = this.jobService.getByJobId(order.getJob().getJobId());

        User user = new User();
        Job job = new Job();
      
        user.setUserId(existedUser.getUserId());
        user.setEmail(existedUser.getEmail());
        user.setUserName(existedUser.getUserName());
        user.setProfilePicture(null);

     
        job.setJobId(existedJob.getJobId());
        job.setJobName(existedJob.getJobName());
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
        request.setConfirmed(false);

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
    public Order updateOrder(Order order) {
        log.info("Updating order");
        Order response = this.orderRepository.findByIdAndConfirmedFalse(order.getId());
        
        if(response!=null){
            log.info("order found");
            response.setCustomerEmail(order.getCustomerEmail());
            response.setCustomerName(order.getCustomerName());
            response.setRequirementFile(order.getRequirementFile());
            response.setCustomerId(order.getCustomerId());
            response.setConfirmed(true);
            response.setDocumentType(order.getDocumentType());
            response = this.orderRepository.save(response);
            
    
            return response;
        }

        return null;
    }

    @Override
    public List<Order> getAllOrdersByCustomerEmail(String email) {
       log.info("Getting all Orders By Email");

       return this.orderRepository.findAllByCustomerEmail(email);
    }

    @Override
    public List<Order> getAllOrdersByCustomerId(String id) {
        log.info("Getting all Orders by customer id: {}",id);

        return this.orderRepository.findByCustomerId(id);

    }

    @Override
    public Order orderDone(Order order) {
        log.info("Updating job status");

        order.setCompleted(true);
        
        return this.orderRepository.save(order);
    }
    

}
