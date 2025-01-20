package com.park.mall.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload.dir}")
    private String fileUploadDir;

    private final SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 임시 업로드 처리
     * @return 생성된 파일명
     */
    public String uploadTemp(MultipartFile file) {
        try {
            if(!file.isEmpty()) {
                File tempDir = new File(getTempPath());
                if (!tempDir.exists()) {
                    tempDir.mkdir();
                }

                String storeFileName = createStoreFileName(file.getOriginalFilename());
                String fullPath = getTempPath() + storeFileName;
                file.transferTo(new File(fullPath));
                return storeFileName;
            } else {
                return null;
            }
        } catch (IOException ex) {
            throw new RuntimeException("파일 작업 중 문제 발생", ex);
        }
    }

    /**
     * 임시 업로드에서 실제 경로로 업로드 처리
     * @return 업로드 경로
     */
    public String uploadFromTemp(String storeFileName) {
        File file = new File(getTempPath() + storeFileName);
        if (!file.exists()) {
            return null;
        }

        String todayStr = dtFormat.format(new Date());
        String realDirStr = fileUploadDir + "/" + todayStr;
        File realDir = new File(realDirStr);
        if (!realDir.exists()) {
            realDir.mkdir();
        }

        String fullPath = realDirStr + "/" + storeFileName;
        File realFile = new File(fullPath);

        try {
            Files.copy(file.toPath(), realFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("IOException 발생", ex);
        }

        return fullPath.replace(fileUploadDir, "");
    }

    public String getTempPath() {
        return fileUploadDir + "/temp/";
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return  originalFilename.substring(pos + 1);
    }
}
