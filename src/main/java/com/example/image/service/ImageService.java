package com.example.image.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String upload(MultipartFile imageFile) throws IOException {

        if (imageFile.isEmpty()) {
            return null;
        }


        String originalFilename = imageFile.getOriginalFilename();


        String storedFilename = createStoredFilename(originalFilename);


        String fullPath = uploadDir + storedFilename;
        log.info("파일 저장 fullPath={}", fullPath);

        imageFile.transferTo(new File(fullPath));


        return "/images/" + storedFilename;
    }

    // 고유한 파일 이름을 생성
    private String createStoredFilename(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            String storedFilename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            File file = new File(uploadDir + storedFilename);

            if (file.exists()) {
                if (file.delete()) {
                    log.info("파일 삭제 성공: {}", storedFilename);
                } else {
                    log.warn("파일 삭제 실패: {}", storedFilename);
                }
            }
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", imageUrl, e);
        }
    }
}
