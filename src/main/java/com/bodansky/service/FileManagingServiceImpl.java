package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileManagingServiceImpl implements IFileManagingService {

    private final static Logger log = LoggerFactory.getLogger(FileManagingServiceImpl.class);

    private static final String S3_PREFIX = "s3://";
    private final ResourceLoader resourceLoader;
    private final AmazonS3 s3;

    @Autowired
    public FileManagingServiceImpl(ResourceLoader resourceLoader, AmazonS3 s3) {
        this.resourceLoader = resourceLoader;
        this.s3 = s3;
    }

    @Override
    public ResponseEntity storeFile(@RequestBody File file, String bucketName, String keyName) {
        log.info("store file {} {}", file.getName(), file.length());
        try {
            s3.putObject(bucketName, keyName, file);
        } catch (AmazonServiceException e) {
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public Resource downLoadFile(String bucketName, String keyName) {
        return resourceLoader.getResource(S3_PREFIX + bucketName + "/" + keyName);
    }

    @Override
    public InputStream serveFile(String bucketName, String keyName) {
        Resource resource = resourceLoader.getResource(S3_PREFIX + bucketName + "/" + keyName);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return inputStream;
    }
}
