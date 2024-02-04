package com.phcworld.domain.board;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(indexes = {
        @Index(name = "idx_diar_hashtag_diary_id_hashtag_id", columnList = "diary_id, hashtag_id")
})
@ToString
public class DiaryHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Diary diary;

    @ManyToOne
    private Hashtag hashtag;

    public String getHashtagName() {
        return this.hashtag.getName();
    }
}
