package com.phcworld.repository.board;

import com.phcworld.domain.answer.QDiaryAnswer;
import com.phcworld.domain.board.QDiary;
import com.phcworld.domain.good.QGood;
import com.phcworld.domain.user.QUser;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryCustomImpl implements DiaryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QDiary diary = QDiary.diary;
    QUser qUser = QUser.user;
    QGood good = QGood.good;
    QDiaryAnswer answer = QDiaryAnswer.diaryAnswer;

    private List<DiarySelectDto> findAllList(User user, Pageable pageable){
        List<Long> ids = queryFactory
                .select(diary.id)
                .from(diary)
//                .leftJoin(diary.writer, user)
//                .where(user.email.eq(keyword))
                .where(eqWriter(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(diary.createDate.desc())
                .fetch();

        return queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id,
                        qUser.as("writer"),
                        diary.title,
                        diary.thumbnail,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(answer.count())
                                        .from(answer)
                                        .where(answer.diary.eq(diary)), "countOfAnswers"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(good.count())
                                        .from(good)
                                        .where(good.diary.eq(diary)), "countOfGood"),
                        diary.updateDate,
                        diary.createDate
//                        good.count().as("countOfGood")
                        ))
                .from(diary)
                .leftJoin(diary.writer, qUser)
//                .leftJoin(good).on(good.diary.eq(diary))
                .where(diary.id.in(ids))
                .orderBy(diary.createDate.desc())
//                .groupBy(diary)
                .fetch();
    }

    private Long getDiaryCount(User user){
        return queryFactory
                .select(diary.count())
                .from(diary)
                .where(eqWriter(user))
                .fetchOne();
    }

    @Override
    public Page<DiarySelectDto> findAllPage(User user, Pageable pageable){
        List<DiarySelectDto> content = findAllList(user, pageable);
        Long count = getDiaryCount(null);
        return new PageImpl<>(content, pageable, count);
    }

    private BooleanExpression eqWriter(User user){
        if(Objects.isNull(user)){
            return null;
        }
        return diary.writer.eq(user);
    }

}
