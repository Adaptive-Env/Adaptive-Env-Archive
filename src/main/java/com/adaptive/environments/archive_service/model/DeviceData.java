package com.adaptive.environments.archive_service.model;

import java.util.Map;

public interface DeviceData {
    String getDeviceId();
    String getSensorId();
    String getLocation();
    String getType();
    Long getTimestamp();
    String getAuthKey();
    Map<String, Object> getData();
}
