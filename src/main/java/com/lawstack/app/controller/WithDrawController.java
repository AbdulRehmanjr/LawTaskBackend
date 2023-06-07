package com.lawstack.app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.WithDraw;
import com.lawstack.app.service.WithDrawService;



@RestController
@RequestMapping("/withdraw")
public class WithDrawController {
    
    @Autowired
    private WithDrawService withDrawService;


    @PostMapping("/create")
    ResponseEntity<?> saveWithDraw(@RequestBody WithDraw withDraw){

        WithDraw response = this.withDrawService.saveWithDraw(withDraw);

        if(response == null){
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(response);
    }
    
    @PostMapping("/update")
    ResponseEntity<?> updateWithDraw(@RequestBody WithDraw withDraw){

        WithDraw response = this.withDrawService.updateDraw(withDraw);

        if(response == null){
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(response);
    }
    @GetMapping("/all")
    ResponseEntity<?> getAllWithDraws(){

        List<WithDraw> response = this.withDrawService.getAllWithDraws();

        if(response == null){
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(response);
    }
    @GetMapping("/{userId}")
    ResponseEntity<?> getAllWithDrawsByUserId(@PathVariable String userId){

        List<WithDraw> response = this.withDrawService.getAllWithDrawsByUserId(userId);

        if(response == null){
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/count")
    ResponseEntity<?> getAllPending(){

        int response = this.withDrawService.getPendingCount(false);

        return ResponseEntity.status(201).body(response);
    }
}
