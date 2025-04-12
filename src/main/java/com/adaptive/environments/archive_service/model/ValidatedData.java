package com.adaptive.environments.archive_service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatedData {
    private DeviceData deviceData;
    private String hash;
}
