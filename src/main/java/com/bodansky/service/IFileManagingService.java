package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import org.springframework.http.ResponseEntity;

import java.io.File;

public interface IFileManagingService {

    ResponseEntity storeFile(File file, String bucketName, String keyName);

    String downLoadFile(String bucketName, String keyName);
}
