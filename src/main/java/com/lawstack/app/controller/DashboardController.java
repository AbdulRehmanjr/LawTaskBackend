package com.lawstack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Dashboard;
import com.lawstack.app.service.DashboardService;



@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    

    @Autowired
    private DashboardService dashboardService;

    
    @GetMapping("/info")
    ResponseEntity<?> getDashBoardInfo(){

        Dashboard info = this.dashboardService.getDashboard(1);

        if(info == null)
            return ResponseEntity.status(404).body(null);
        return ResponseEntity.status(201).body(info);
    }

}
