package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileManagingServiceImpl implements IFileManagingService {

    private final static Logger log = LoggerFactory.getLogger(FileManagingServiceImpl.class);

    private static final String S3_PREFIX = "s3://";
    private final ResourceLoader resourceLoader;


    @Autowired
    public FileManagingServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResponseEntity storeFile(MultipartFile multipartFile, String bucketName, String keyName) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        if (multipartFile.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        try {
            s3.putObject(bucketName, keyName, convertMultipartToFile(multipartFile));
        } catch (AmazonServiceException | IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public InputStream serveFile(String bucketName, String keyName) throws IOException {
        Resource resource = this.resourceLoader.getResource(S3_PREFIX + bucketName + "/" + keyName);
        return resource.getInputStream();
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File fileToConvert = new File(file.getOriginalFilename());
        file.transferTo(fileToConvert);
        return fileToConvert;
    }
}
