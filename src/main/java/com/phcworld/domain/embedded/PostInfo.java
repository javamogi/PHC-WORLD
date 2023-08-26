package com.phcworld.domain.embedded;

import com.phcworld.domain.common.SaveType;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostInfo {
    @Enumerated(EnumType.STRING)
    private SaveType saveType;

    private Long postId;
    private Long redirectId;
}
