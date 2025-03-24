package com.adaptive.environments.archive_service.service;

import com.adaptive.environments.archive_service.model.ValidatedData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ArchiveService {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;
    private final String bucketName;
    private final MeterRegistry meterRegistry;

    public ArchiveService(S3Client s3Client,
                          @Value("${minio.bucket}") String bucketName,
                          MeterRegistry meterRegistry) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.objectMapper = new ObjectMapper();
        this.meterRegistry = meterRegistry;
    }

    public void store(ValidatedData data) {
        try {
            String key = LocalDate.now() + "/" + UUID.randomUUID() + ".json";
            String json = objectMapper.writeValueAsString(data);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("application/json")
                    .build();

            s3Client.putObject(request, RequestBody.fromString(json));

            meterRegistry.counter("archive.success").increment();
            meterRegistry.summary("archive.size.bytes").record(json.getBytes().length);

            System.out.println("[Archive] Data archived as: " + key);
        } catch (Exception e) {
            meterRegistry.counter("archive.failure").increment();
            System.err.println("[Archive] Failed to store data: " + e.getMessage());
        }
    }
}

