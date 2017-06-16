package com.bodansky.controller;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import com.bodansky.service.FileManagingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/fms")
public class FileManagingController {

    private final static Logger log = LoggerFactory.getLogger(FileManagingController.class);

    private final FileManagingServiceImpl fileManagingServiceImpl;

    @Autowired
    public FileManagingController(FileManagingServiceImpl fileManagingServiceImpl) {
        this.fileManagingServiceImpl = fileManagingServiceImpl;
    }

    @PostMapping("/fileUpload")
    public ResponseEntity uploadFile(@RequestBody MultipartFile file, @RequestParam String bucketName, @RequestParam String keyName) {
        log.info("upload file to bucket/keyName {} {} {}",file.getOriginalFilename(),  bucketName, keyName);
        return fileManagingServiceImpl.storeFile(file, bucketName, keyName);
    }

    @GetMapping("/fileDownload")
    public ResponseEntity<File> downloadFile(@RequestParam String bucketName, @RequestParam String keyName) {
        log.info("download file bucketName/keyName {} {}",bucketName, keyName);
        return fileManagingServiceImpl.serveFile(bucketName, keyName);
    }
}
