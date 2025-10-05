package com.adaptive.environments.archive_service.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Component("s3BucketInit")
public class S3BucketInit {
    private static final Logger log = LoggerFactory.getLogger(S3BucketInit.class);

    private final S3Client s3;
    private final String bucket;
    private final String region;

    public S3BucketInit(
            S3Client s3,
            @Value("${minio.bucket}") String bucket,
            @Value("${minio.region:us-east-1}") String region
    ) {
        this.s3 = s3;
        this.bucket = bucket;
        this.region = region;
    }

    @PostConstruct
    public void ensureBucket() {
        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            log.info("S3 bucket '{}' already exists", bucket);
        } catch (S3Exception e) {
            log.info("S3 bucket '{}' not found, creating...", bucket);
            CreateBucketRequest.Builder b = CreateBucketRequest.builder().bucket(bucket);
            if (!"us-east-1".equalsIgnoreCase(region)) {
                b.createBucketConfiguration(
                        CreateBucketConfiguration.builder()
                                .locationConstraint(region)
                                .build()
                );
            }
            s3.createBucket(b.build());
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            log.info("S3 bucket '{}' created", bucket);
        }
    }
}