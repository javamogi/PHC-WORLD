package com.phcworld.repository.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.QAlert;
import com.phcworld.domain.alert.dto.AlertSelectDto;
import com.phcworld.domain.answer.QDiaryAnswer;
import com.phcworld.domain.answer.QFreeBoardAnswer;
import com.phcworld.domain.board.QDiary;
import com.phcworld.domain.board.QFreeBoard;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.good.QGood;
import com.phcworld.domain.user.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryCustomImpl implements AlertRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QAlert alert = QAlert.alert;
    QFreeBoard freeBoard = QFreeBoard.freeBoard;
    QDiary diary = QDiary.diary;

    public List<AlertSelectDto> findAlertListByPostWriter(User user){
        return queryFactory
                .select(Projections.fields(AlertSelectDto.class,
                        alert.id,
                        alert.postInfo.saveType,
                        diary.title.as("diaryTitle"),
                        freeBoard.title.as("freeBoardTitle"),
                        alert.registerUser.name.as("userName"),
                        alert.createDate
                        ))
                .from(alert)
                .leftJoin(diary).on(diary.id.eq(alert.postInfo.redirectId)
                        .and(alert.postInfo.saveType.in(SaveType.GOOD, SaveType.DIARY_ANSWER)))
                .leftJoin(freeBoard).on(freeBoard.id.eq(alert.postInfo.redirectId)
                        .and(alert.postInfo.saveType.eq(SaveType.FREE_BOARD_ANSWER)))
                .where(alert.postWriter.eq(user))
                .limit(5)
                .offset(0)
                .orderBy(alert.createDate.desc())
                .fetch();
    }

}
