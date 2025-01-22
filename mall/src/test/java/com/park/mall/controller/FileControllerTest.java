package com.park.mall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.park.mall.config.SecurityConfig;
import com.park.mall.service.file.FileService;
import com.park.mall.web.admin.file.FileController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(FileController.class)
@Import(SecurityConfig.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileService fileService;

    private MockMultipartFile multipartFile;

    @BeforeEach
    public void setup() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/static/img/test.jpg");
        multipartFile = new MockMultipartFile(
                "file", // 요청 파라미터 이름
                "test.jpg",   // 원래 파일 이름
                "image/jpeg", // MIME 타입
                inputStream
        );
    }

    @Test
    void tempUpload() throws Exception {
        //given
        Mockito.when(fileService.uploadTemp(Mockito.any(MultipartFile.class)))
                .thenReturn("tempFileName");

        //when
        ResultActions mvcAction = mockMvc.perform(
                multipart("/admin/file/temp/upload")
                        .file(multipartFile)
                        .with(csrf())
        );

        //then
        MvcResult mvcResult = mvcAction.andExpect(status().isOk())
                .andReturn();

        var contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.readValue(contentAsString, Map.class);
        Assertions.assertEquals("tempFileName", map.get("fileName"));
    }

    @Test
    void tempUploadThrowEx() throws Exception {
        //given
        Mockito.when(fileService.uploadTemp(Mockito.any(MultipartFile.class)))
                .thenThrow(new RuntimeException("IO Exception"));

        //when
        ResultActions mvcAction = mockMvc.perform(
                multipart("/admin/file/temp/upload")
                        .file(multipartFile)
                        .with(csrf())
        );

        //then
        mvcAction.andExpect(status().isInternalServerError());
    }

    @Test
    void downloadFile() throws Exception {
        //given
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resource.exists()).thenReturn(true);
        Mockito.when(resource.getFilename()).thenReturn("fileName.jpg");

        Mockito.when(fileService.getResource(Mockito.any(String.class)))
                .thenReturn(resource);

        //when
        ResultActions mvcAction = mockMvc.perform(
                get("/admin/file/download")
                        .param("filePath", "temp/fileName.jpg")
        );

        //then
        mvcAction.andExpect(status().isOk());
    }
}
