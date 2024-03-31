package com.phcworld.common.service;

import com.phcworld.common.infrastructure.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {
    @Override
    public String random() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
