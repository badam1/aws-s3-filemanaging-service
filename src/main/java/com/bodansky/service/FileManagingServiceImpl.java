package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.net.URL;

import static com.amazonaws.HttpMethod.GET;

@Service
public class FileManagingServiceImpl implements IFileManagingService {

    private final static Logger log = LoggerFactory.getLogger(FileManagingServiceImpl.class);

    private final AmazonS3 s3;

    @Autowired
    public FileManagingServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public ResponseEntity<String> storeFile(@RequestBody File file, String bucketName, String keyName) {
        log.info("store file {} {}", file.getName(), file.length());
        String downloadUrl = null;
        try {
            s3.putObject(bucketName, keyName, file);
            URL url = getUrl(bucketName, keyName);
            downloadUrl = url.toString();
        } catch (AmazonServiceException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(downloadUrl,HttpStatus.OK);
    }

    @Override
    public String downLoadFile(String bucketName, String keyName) {
        URL url = getUrl(bucketName, keyName);
        log.info("generated url  {}", url.toString());
        return url.toString();
    }

    private URL getUrl(String bucketName, String keyName) {
        return s3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, keyName, GET));
    }
}
