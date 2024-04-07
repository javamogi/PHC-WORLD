package com.phcworld.mock;

import com.phcworld.common.infrastructure.UuidHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FakeUuidHolder implements UuidHolder {

    private final String uuid;

    @Override
    public String random() {
        return uuid;
    }
}
