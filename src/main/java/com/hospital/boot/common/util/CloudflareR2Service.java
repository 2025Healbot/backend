package com.hospital.boot.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudflareR2Service {

    private final S3Client s3Client;

    @Value("${cloud.r2.bucket}")
    private String bucketName;

    @Value("${cloud.r2.public-url}")
    private String publicUrl;

    /**
     * 파일을 Cloudflare R2에 업로드
     * @param file 업로드할 파일
     * @param folder R2 버킷 내 폴더 경로 (예: "event/thumbnail", "lecture/videos")
     * @return 업로드된 파일의 공개 URL
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : "";

        String fileName = UUID.randomUUID().toString() + extension;
        String key = folder.endsWith("/") ? folder + fileName : folder + "/" + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            String fileUrl = publicUrl + "/" + key;
            return fileUrl;
        } catch (Exception e) {
            throw new IOException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파일을 Cloudflare R2에 업로드 (파일명 지정)
     * @param file 업로드할 파일
     * @param folder R2 버킷 내 폴더 경로
     * @param fileName 저장할 파일명
     * @return 업로드된 파일의 공개 URL
     */
    public String uploadFile(MultipartFile file, String folder, String fileName) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String key = folder.endsWith("/") ? folder + fileName : folder + "/" + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            String fileUrl = publicUrl + "/" + key;
            return fileUrl;
        } catch (Exception e) {
            throw new IOException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * Cloudflare R2에서 파일 삭제
     * @param fileUrl 삭제할 파일의 공개 URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // URL에서 키 추출
            String key = fileUrl.replace(publicUrl + "/", "");

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            // 삭제 실패 무시
        }
    }

    /**
     * File 객체를 Cloudflare R2에 업로드
     * @param file 업로드할 파일
     * @param folder R2 버킷 내 폴더 경로
     * @param fileName 저장할 파일명
     * @param contentType Content-Type
     * @return 업로드된 파일의 공개 URL
     */
    public String uploadFile(java.io.File file, String folder, String fileName, String contentType) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        String key = folder.endsWith("/") ? folder + fileName : folder + "/" + fileName;

        try (InputStream inputStream = new java.io.FileInputStream(file)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(file.length())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.length()));

            String fileUrl = publicUrl + "/" + key;
            return fileUrl;
        } catch (Exception e) {
            throw new IOException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 여러 파일을 한번에 삭제
     * @param fileUrls 삭제할 파일들의 공개 URL 배열
     */
    public void deleteFiles(String... fileUrls) {
        if (fileUrls == null || fileUrls.length == 0) {
            return;
        }

        for (String fileUrl : fileUrls) {
            deleteFile(fileUrl);
        }
    }

    /**
     * 파일이 존재하는지 확인
     * @param fileUrl 확인할 파일의 공개 URL
     * @return 파일 존재 여부
     */
    public boolean fileExists(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            String key = fileUrl.replace(publicUrl + "/", "");

            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
