package com.adaptive.environments.archive_service.kafka;

import com.adaptive.environments.archive_service.model.ValidatedData;
import com.adaptive.environments.archive_service.service.ArchiveService;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;

@Component
public class ValidatedDataReceiver {

    private final KafkaReceiver<String, ValidatedData> kafkaReceiver;
    private final ArchiveService archiveService;

    public ValidatedDataReceiver(KafkaReceiver<String, ValidatedData> kafkaReceiver,
                                      ArchiveService archiveService) {
        this.kafkaReceiver = kafkaReceiver;
        this.archiveService = archiveService;
        start();
    }

    private void start() {
        kafkaReceiver.receive()
                .doOnNext(record -> {
                    archiveService.store(record.value());
                    record.receiverOffset().acknowledge();
                })
                .subscribe();
    }
}
