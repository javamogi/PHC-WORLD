package com.phcworld.repository.alert;

import com.phcworld.domain.alert.QAlert;
import com.phcworld.domain.alert.dto.AlertSelectDto;
import com.phcworld.domain.board.QDiary;
import com.phcworld.domain.common.SaveType;
import com.phcworld.freeboard.infrastructure.QFreeBoardEntity;
import com.phcworld.user.infrastructure.UserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryCustomImpl implements AlertRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QAlert alert = QAlert.alert;
    QFreeBoardEntity freeBoard = QFreeBoardEntity.freeBoardEntity;
    QDiary diary = QDiary.diary;

    public List<AlertSelectDto> findAlertListByPostWriter(UserEntity user){
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

    @Override
    public void deleteAlert(SaveType saveType, Long id) {
        queryFactory
                .delete(alert)
                .where(eqSaveType(saveType, id))
                .execute();
    }

    private BooleanExpression eqSaveType(SaveType saveType, Long id){
        if(saveType == SaveType.DIARY){
            return alert.postInfo.redirectId.eq(id)
                    .and(alert.postInfo.saveType.in(SaveType.DIARY, SaveType.DIARY_ANSWER, SaveType.GOOD));
        } else if (saveType == SaveType.FREE_BOARD){
            return alert.postInfo.redirectId.eq(id)
                    .and(alert.postInfo.saveType.in(SaveType.FREE_BOARD, SaveType.FREE_BOARD_ANSWER));
        }
        return alert.postInfo.postId.eq(id)
                .and(alert.postInfo.saveType.eq(saveType));
    }

}
