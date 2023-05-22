package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.FileSharing;
import com.lawstack.app.model.Order;
import com.lawstack.app.repository.FileRespository;
import com.lawstack.app.service.FileService;
import com.lawstack.app.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImp  implements FileService{
    
    @Autowired
    private FileRespository fileRepo;

    @Autowired
    private OrderService orderService;
    
    @Override
    public FileSharing saveFile(FileSharing fileSharing) {
        
        log.info("saving file of project in data base");

        String id = UUID.randomUUID().toString();

        Order order = this.orderService.getOrderById(fileSharing.getOrder().getId());
        fileSharing.setId(id);
        fileSharing.setOrder(order);
        return this.fileRepo.save(fileSharing);
    }

    @Override
    public List<FileSharing> getFiles(String orderId) {
        
        log.info("Geting all files from database accoriing to orderId : ");

        return this.fileRepo.findAllByOrderId(orderId);
    }
    
}
