package com.lawstack.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.WithDraw;

public interface WithDrawRepository extends JpaRepository<WithDraw,String>{
    
    List<WithDraw> findAllByUserId(String userId);
}
