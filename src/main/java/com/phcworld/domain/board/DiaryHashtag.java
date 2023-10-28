package com.phcworld.domain.board;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(indexes = @Index(name = "idx__diary_id", columnList = "diary_id"))
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
