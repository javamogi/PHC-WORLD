package com.phcworld.repository.timeline;

import com.phcworld.domain.common.SaveType;

public interface TimelineRepositoryCustom {
    void deleteTimeline(SaveType saveType, Long id);
}
