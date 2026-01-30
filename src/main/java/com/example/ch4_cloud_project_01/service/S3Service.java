package com.example.ch4_cloud_project_01.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    // 과제 조건: 7일
    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);

    // S3 업로드
    public String upload(MultipartFile file) {
        try {
            String key = "profiles/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            s3Template.upload(bucket, key, file.getInputStream());
            return key;
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

     // Presigned URL 생성
     public URL getPresignedUrl(String key) {
         return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
     }
}
