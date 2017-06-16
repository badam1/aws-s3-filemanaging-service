package com.bodansky.controller;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import com.amazonaws.util.IOUtils;
import com.bodansky.service.IFileManagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

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
    public ResponseEntity downloadFile(@PathVariable String bucketName, @PathVariable String userId, @PathVariable String fileName, HttpServletResponse response) {
        String keyName = userId + "/" + fileName;
        log.info("download file bucketName/keyName {} {}", bucketName, keyName);
        try {
            addDownloadResponseHeaders(response, keyName);
            IOUtils.copy(fileManagingService.serveFile(bucketName, keyName), response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private void addDownloadResponseHeaders(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
    }
}
