package com.example.bmillion_backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.bmillion_backend.core.error.ErrorCode;
import com.example.bmillion_backend.core.error.exception.S3Exception;
import com.example.bmillion_backend.entity.PostEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public void uploadFile(PostEntity post, MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e){
            throw new S3Exception("파일 업로드에 실패했습니다", ErrorCode.NOT_FOUND_EXCEPTION);
        }

        post.setFileName(s3FileName);
        post.setFileUrl(amazonS3.getUrl(bucket, s3FileName).toString());
    }

    public void deleteFile(String filename) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, filename));
    }

}
