package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface IFileManagingService {

    ResponseEntity storeFile(MultipartFile multipartFile, String bucketName, String keyName);

    InputStream serveFile(String bucketName, String keyName) throws IOException;
}
