package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer>{
    
    
}
