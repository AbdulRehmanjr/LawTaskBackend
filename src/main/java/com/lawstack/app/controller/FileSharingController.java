package com.lawstack.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.model.FileSharing;
import com.lawstack.app.service.FileService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/file")
public class FileSharingController {
    
    @Autowired
    private FileService fileService;

    @PostMapping("/save")
    ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file, String metaData){

        log.info("File uploading request found");

        FileSharing sharing = new FileSharing();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            sharing = objectMapper.readValue(metaData, FileSharing.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        try {
            sharing.setFile(file.getBytes());
            sharing.setFileType(file.getContentType());
            sharing.setFileName(file.getOriginalFilename());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }
        FileSharing response  = this.fileService.saveFile(sharing);

        if(response ==null){
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/order/{orderId}")
    ResponseEntity<?> getFilesByOrder(@PathVariable String orderId){

        List<FileSharing> files = this.fileService.getFiles(orderId);

        if(files==null){

            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(files);
    }
}
