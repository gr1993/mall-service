package com.park.mall.service;

import com.park.mall.service.file.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.InputStream;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Value("${file.upload.dir}")
    private String fileUploadDir;

    @Value("${file.temp.dir}")
    private String fileTempDir;

    InputStream inputStream;

    @BeforeEach
    public void setup() {
        inputStream = getClass().getResourceAsStream("/static/img/test.jpg");
    }

    @Test
    void fileTempUpload() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", // 요청 파라미터 이름
                "test.jpg",   // 원래 파일 이름
                "image/jpeg", // MIME 타입
                inputStream
        );

        //when
        String fileName = fileService.uploadTemp(multipartFile);

        //then
        File file = new File(fileService.getTempPath() + "/" + fileName);
        Assertions.assertTrue(file.exists());
    }

    @Test
    void uploadFromTemp() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", // 요청 파라미터 이름
                "test.jpg",   // 원래 파일 이름
                "image/jpeg", // MIME 타입
                inputStream
        );
        String fileName = fileService.uploadTemp(multipartFile);

        //when
        String uploadPath = fileService.uploadFromTemp(fileName);

        //then
        File file = new File(fileUploadDir + "/" + uploadPath);
        Assertions.assertTrue(file.exists());
    }

    @Test
    void getResource() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", // 요청 파라미터 이름
                "test.jpg",   // 원래 파일 이름
                "image/jpeg", // MIME 타입
                inputStream
        );
        String fileName = fileService.uploadTemp(multipartFile);
        String filePath = fileTempDir + "/" + fileName;

        //when
        Resource resource = fileService.getResource(filePath);

        //then
        Assertions.assertNotNull(resource);
        Assertions.assertTrue(resource.exists());
    }
}
