package com.phcworld.repository.timeline;

import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.timeline.QTimeline;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimelineRepositoryCustomImpl implements TimelineRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QTimeline timeline = QTimeline.timeline;

    @Override
    public void deleteDiaryTimeline(Long diaryId){
        queryFactory
                .delete(timeline)
                .where(timeline.postInfo.redirectId.eq(diaryId)
                        .and(timeline.postInfo.saveType.in(SaveType.DIARY, SaveType.DIARY_ANSWER, SaveType.GOOD)))
                .execute();
    }

}
