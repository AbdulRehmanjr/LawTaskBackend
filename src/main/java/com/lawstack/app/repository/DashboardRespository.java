package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Dashboard;

public interface DashboardRespository extends JpaRepository<Dashboard,Integer>{
    
}
