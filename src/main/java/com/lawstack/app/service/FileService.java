package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.FileSharing;

public interface FileService {
    
    FileSharing saveFile(FileSharing fileSharing);

    List<FileSharing> getFiles(String orderId);

}
