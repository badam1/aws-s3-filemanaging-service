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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        Path path = Paths.get(rootLocation + "/1/test.txt");
        String name = "test.txt";
        String originalFileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);
        ResponseEntity responseEntity = fileManagingService.storeFile(result, "mfm-server-files", "1/test.txt");
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDownloadFile() throws IOException {
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
}
