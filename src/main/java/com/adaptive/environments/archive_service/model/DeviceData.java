package com.adaptive.environments.archive_service.model;

import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceData {
    private String deviceId;
    private String sensorId;
    private String location;
    private String type;
    private Long timestamp;
    private String authKey;
    private Map<String, Object> data;
}
