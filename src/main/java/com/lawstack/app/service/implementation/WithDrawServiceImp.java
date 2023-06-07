package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Dashboard;
import com.lawstack.app.model.User;
import com.lawstack.app.model.UserDashboard;
import com.lawstack.app.model.WithDraw;
import com.lawstack.app.repository.WithDrawRepository;
import com.lawstack.app.service.DashboardService;
import com.lawstack.app.service.EmailService;
import com.lawstack.app.service.UserDashBoardService;
import com.lawstack.app.service.UserService;
import com.lawstack.app.service.WithDrawService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WithDrawServiceImp implements WithDrawService{
    
    @Autowired
    private WithDrawRepository withDrawRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDashBoardService udService;

    @Autowired
    private DashboardService adminDashService;

    @Autowired
    private EmailService emailService;

    @Value("${admin_mail}")
    private String ADMIN;

    @Override
    public WithDraw saveWithDraw(WithDraw withDraw) {
       
        String id = UUID.randomUUID().toString();

       
        User user = this.userService.getUserById(withDraw.getUser().getUserId());
        if(user == null){
            log.error("User not found.");
            return null;
        }
        withDraw.setId(id);
        withDraw.setUser(user);
        WithDraw response = this.withDrawRepository.save(withDraw);

        String content = """
                User : %s wants to withdraw %s amount.
                """.formatted(user.getEmail(),withDraw.getAmount());
                try {
                    this.emailService.sendMail(ADMIN, "Withdraw Request", content);
                    this.emailService.sendMail(user.getEmail(), "Request of widthDraw", "Request forwarded.");
                } catch (Exception e) {
                    log.error("Error in sending email. {}",e.getMessage());
                }
       
        if(response == null){
            log.error("Error in saving withdraw");
            return null;
        }
        return response;
    }

    @Override
    public WithDraw updateDraw(WithDraw withDraw) {

        
        WithDraw response = this.withDrawRepository.findById(withDraw.getId()).get();
        
        if(response == null){
            log.error("Error in fetching withdraw");
            return null;
        }   

        response.setStatus(true);
        
        


      
        UserDashboard udash = this.udService.getInfoByUserId(response.getUser().getUserId());
        Dashboard dashboard = this.adminDashService.getDashboard(1);

        if(udash ==null || dashboard == null){
            log.error("Error dashboard or user dashboard not found");
            return null;
        }
        udash.setRevenue(udash.getRevenue()-withDraw.getAmount());
        dashboard.setIncome(dashboard.getIncome()-withDraw.getAmount());

        dashboard = this.adminDashService.updateAmount(dashboard);
        udash = this.udService.updateDashboard(udash);

        response = this.withDrawRepository.save(response);

        
        
        String content = """
                User : %s your request for amount  withdraw of %s had being accepted.
                """.formatted(response.getUser().getEmail(),withDraw.getAmount());
                try {
                    this.emailService.sendMail(response.getUser().getEmail(), "Withdraw Request Approved", content);
                    this.emailService.sendMail(ADMIN, "Request of widthDraw", "You accepted the request of user  "+response.getUser().getEmail());
                } catch (Exception e) {
                    log.error("Error in sending email. {}",e.getMessage());
                }

        if(response == null||udash ==null || dashboard == null){
            log.error("cant update user withdraw");
            return null;
        } 
        return response;
    }

    @Override
    public List<WithDraw> getAllWithDrawsByUserId(String userId) {
       List<WithDraw> response = this.withDrawRepository.findAllByUserUserId(userId);

       if(response== null){
            log.error("No WithDraw History Found");
            return null;
       }
       return response;
    }

    @Override
    public List<WithDraw> getAllWithDraws() {
        List<WithDraw> response = this.withDrawRepository.findAll();

        if(response== null){
             log.error("No WithDraw");
             return null;
        }
        return response;
    }

    @Override
    public int getPendingCount(boolean status) {

        log.info("Geting counts of withdraw Pending.");
        int  response = this.withDrawRepository.findAllByStatus(false).size();

        return response;
    }
    
}
