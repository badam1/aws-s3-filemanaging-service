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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

import static org.junit.Assert.assertEquals;

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
    public void testServeFile() throws IOException {
        InputStream is = fileManagingService.serveFile("mfm-server-files", "1/test.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        log.info("file {}", sb.toString());
        String fileContent = "Test content";
        assertEquals(fileContent, sb.toString());
    }

    @Test
    public void testDownloadFile() throws IOException {
        Resource resource = fileManagingService.downLoadFile("mfm-server-files", "1/test.txt");
        log.info("downloaded file name and size {} {}", resource.getFilename(), resource.contentLength());
        assertEquals(resource.getFilename(), "1/test.txt");
    }
}
