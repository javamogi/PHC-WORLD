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

    public String getRedirectUrl(){
        if(this.saveType == SaveType.DIARY
                || this.saveType == SaveType.DIARY_ANSWER
                || this.saveType == SaveType.GOOD){
            return "redirect:/diaries/" + getRedirectId();
        }
        return "redirect:/freeboards/" + getRedirectId();
    }
}
