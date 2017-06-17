package com.bodansky.service;

/*
 * Created by Adam Bodansky on 2017.06.16..
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileManagingServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(FileManagingServiceImplTest.class);

    @Value("${root.location}")
    private String rootLocation;

    @Autowired
    private IFileManagingService fileManagingService;

    @Test
    public void testUploadFile() {
        File file = new File(rootLocation + "1/test.txt");
        ResponseEntity responseEntity = fileManagingService.storeFile(file, "mfm-server-files", "1/test.txt");
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDownloadFile() throws IOException {
        String url = fileManagingService.downLoadFile("mfm-server-files", "1/test.txt");
        log.info("download url {}",url);
        assertTrue(url.contains("1/test.txt"));
    }
}
