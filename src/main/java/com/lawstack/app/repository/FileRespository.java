package com.lawstack.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.FileSharing;

public interface FileRespository extends JpaRepository<FileSharing,String>{

    List<FileSharing> findAllByOrderId(String orderId);
}
