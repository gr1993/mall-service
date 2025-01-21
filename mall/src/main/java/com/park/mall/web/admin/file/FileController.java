package com.park.mall.web.admin.file;

import com.park.mall.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @Value("${file.temp.dir}")
    private String fileTempDir;

    @PostMapping("/file/temp/upload")
    public Map<String, Object> tempUpload(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.uploadTemp(file);

        Map<String, Object> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("fileTempDir", fileTempDir);
        return response;
    }

    @GetMapping("/file/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filePath") String filePath) {
        Resource resource = fileService.getResource(filePath);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
