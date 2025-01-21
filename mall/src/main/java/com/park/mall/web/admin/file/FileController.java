package com.park.mall.web.admin.file;

import com.park.mall.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 관리자 > 파일 관련 컨트롤러
 */
@RestController
@RequestMapping("/admin")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file/temp/upload")
    public Map<String, Object> tempUpload(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.uploadTemp(file);

        Map<String, Object> response = new HashMap<>();
        response.put("fileName", fileName);
        return response;
    }
}
