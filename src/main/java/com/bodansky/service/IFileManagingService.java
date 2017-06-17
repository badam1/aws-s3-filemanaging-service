package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.InputStream;

public interface IFileManagingService {

    ResponseEntity storeFile(File file, String bucketName, String keyName);

    InputStream serveFile(String bucketName, String keyName);

    Resource downLoadFile(String bucketName, String keyName);
}
