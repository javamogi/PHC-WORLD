package com.phcworld.repository.dashboard;

import com.phcworld.domain.alert.QAlert;
import com.phcworld.domain.answer.QDiaryAnswer;
import com.phcworld.domain.answer.QFreeBoardAnswer;
import com.phcworld.domain.board.QDiary;
import com.phcworld.domain.board.QFreeBoard;
import com.phcworld.domain.dashboard.dto.DashBoardSelectDto;
import com.phcworld.domain.timeline.QTimeline;
import com.phcworld.user.infrastructure.QUserEntity;
import com.phcworld.user.infrastructure.UserEntity;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DashBoardRepositoryCustomImpl implements DashBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QFreeBoard freeBoard = QFreeBoard.freeBoard;
    QFreeBoardAnswer freeBoardAnswer = QFreeBoardAnswer.freeBoardAnswer;
    QDiary diary = QDiary.diary;
    QDiaryAnswer diaryAnswer = QDiaryAnswer.diaryAnswer;
    QAlert alert = QAlert.alert;
    QUserEntity quser = QUserEntity.userEntity;
    QTimeline timeline = QTimeline.timeline;

    public DashBoardSelectDto findActiveCountByUser(UserEntity user){
        return queryFactory
                .select(Projections.fields(DashBoardSelectDto.class,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(freeBoard.count())
                                        .from(freeBoard)
                                        .where(freeBoard.writer.eq(user)), "freeBoardCount"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(freeBoardAnswer.count())
                                        .from(freeBoardAnswer)
                                        .where(freeBoardAnswer.writer.eq(user)), "freeBoardAnswerCount"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(diary.count())
                                        .from(diary)
                                        .where(diary.writer.eq(user)), "diaryCount"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(diaryAnswer.count())
                                        .from(diaryAnswer)
                                        .where(diaryAnswer.writer.eq(user)), "diaryAnswerCount"),
                        alert.count().as("alertCount")
                        ))
                .from(alert)
                .where(alert.postWriter.eq(user))
                .fetchOne();
    }
}
