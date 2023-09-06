package com.phcworld.repository.timeline;

import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.timeline.QTimeline;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimelineRepositoryCustomImpl implements TimelineRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QTimeline timeline = QTimeline.timeline;

    @Override
    public void deleteTimeline(SaveType saveType, Long id){
        queryFactory
                .delete(timeline)
                .where(eqSaveType(saveType, id))
                .execute();
    }

    private BooleanExpression eqSaveType(SaveType saveType, Long id){
        if(saveType == SaveType.DIARY){
            return timeline.postInfo.redirectId.eq(id)
                    .and(timeline.postInfo.saveType.in(SaveType.DIARY, SaveType.DIARY_ANSWER, SaveType.GOOD));
        } else if (saveType == SaveType.FREE_BOARD){
            return timeline.postInfo.redirectId.eq(id)
                    .and(timeline.postInfo.saveType.in(SaveType.FREE_BOARD, SaveType.FREE_BOARD_ANSWER));
        }
        return timeline.postInfo.postId.eq(id)
                .and(timeline.postInfo.saveType.eq(saveType));
    }
}
