package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.bodansky.controller.FileManagingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileManagingServiceImpl implements IFileManagingService {

    private final static Logger log = LoggerFactory.getLogger(FileManagingController.class);

    @Override
    public ResponseEntity storeFile(MultipartFile multipartFile, String bucketName, String keyName) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            s3.putObject(bucketName, keyName, convertMultipartToFile(multipartFile));
        } catch (AmazonServiceException | IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<File> serveFile(String bucketName, String keyName) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        File file = null;
        try {
            S3Object s3Object = s3.getObject(bucketName, keyName);
            file = convertS3ObjectToFile(s3Object, keyName);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File fileToConvert = new File(file.getOriginalFilename());
        file.transferTo(fileToConvert);
        return fileToConvert;
    }

    private File convertS3ObjectToFile(S3Object s3Object, String keyName) throws IOException {
        S3ObjectInputStream s3is = s3Object.getObjectContent();
        File file = new File(keyName);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] read_buf = new byte[1024];
        int read_len;
        while ((read_len = s3is.read(read_buf)) > 0) {
            fos.write(read_buf, 0, read_len);
        }
        s3is.close();
        fos.close();
        return file;
    }
}
