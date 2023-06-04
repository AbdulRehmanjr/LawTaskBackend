package com.lawstack.app.controller;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer>{
    
}
