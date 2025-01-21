package com.park.mall.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload.dir}")
    private String fileUploadDir;

    @Value("${file.temp.dir}")
    private String fileTempDir;

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
                String fullPath = getTempPath() + "/" + storeFileName;
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
        File file = new File(getTempPath() + "/" + storeFileName);
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

    public Resource getResource(String filePath) {
        try {
            String[] filePathSplit = filePath.split("/");
            String dir = "";
            for (int i = 0; i < filePathSplit.length - 1; i++) {
                dir = filePathSplit[i] + "/";
            }
            Path path = Paths.get(fileUploadDir + "/" + dir)
                    .resolve(filePathSplit[filePathSplit.length - 1])
                    .normalize();
            return new UrlResource(path.toUri());
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Resource Exception", ex);
        }
    }

    public String getTempPath() {
        return fileUploadDir + "/" + fileTempDir;
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
