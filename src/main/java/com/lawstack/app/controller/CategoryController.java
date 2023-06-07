package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Category;
import com.lawstack.app.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    
    @Autowired
    private CategoryRepository catRepo;


    @GetMapping("/all")
    ResponseEntity<?> getAllCategories(){

        List<Category> categories = this.catRepo.findAll();

        try {
            if(categories == null || categories.isEmpty()){
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            log.error("Error can't fetch Categories");
        }

        return ResponseEntity.status(201).body(categories);
    }
}
