package com.bodansky.controller;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import com.bodansky.service.IFileManagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/fms")
public class FileManagingController {

    private final static Logger log = LoggerFactory.getLogger(FileManagingController.class);

    private final IFileManagingService fileManagingService;

    @Autowired
    public FileManagingController(IFileManagingService fileManagingService) {
        this.fileManagingService = fileManagingService;
    }

    @PostMapping("/fileUpload/{bucketName}/{userId}/{fileName:.+}")
    public ResponseEntity uploadFile(@RequestBody File file, @PathVariable String bucketName, @PathVariable String userId, @PathVariable String fileName) {
        String keyName = userId + "/" + fileName;
        log.info("upload file to bucket/keyName {} {} {}", file.getName(), bucketName, keyName);
        return fileManagingService.storeFile(file, bucketName, keyName);
    }

    @GetMapping("/fileDownload/{bucketName}/{userId}/{fileName:.+}")
    public ResponseEntity<String> getFileDownloadUrl(@PathVariable String bucketName, @PathVariable String userId, @PathVariable String fileName) {
        String keyName = userId + "/" + fileName;
        log.info("download file bucketName/keyName {} {}", bucketName, keyName);
        String downloadUrl = fileManagingService.downLoadFile(bucketName, keyName);
        return new ResponseEntity<>(downloadUrl, HttpStatus.OK);
    }
}
