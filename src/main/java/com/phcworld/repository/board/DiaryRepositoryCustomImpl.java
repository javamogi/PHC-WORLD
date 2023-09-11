package com.phcworld.repository.board;

import com.phcworld.domain.answer.QDiaryAnswer;
import com.phcworld.domain.board.QDiary;
import com.phcworld.domain.good.QGood;
import com.phcworld.domain.user.QUser;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DiaryRepositoryCustomImpl implements DiaryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QDiary diary = QDiary.diary;
    QUser qUser = QUser.user;
    QGood good = QGood.good;
    QDiaryAnswer answer = QDiaryAnswer.diaryAnswer;

    private List<DiarySelectDto> findAllList(User user, Pageable pageable){

        List<OrderSpecifier> orders = getOrderSpecifier(pageable);

//        List<Long> ids = queryFactory
//                .select(diary.id)
//                .from(diary)
//                .leftJoin(good).on(good.diary.eq(diary))
////                .leftJoin(diary.writer, user)
////                .where(user.email.eq(keyword))
//                .where(eqWriter(user))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
////                .orderBy(diary.createDate.desc())
////                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
//                .orderBy(aliasCount.desc(), diary.createDate.desc())
//                .groupBy(diary)
//                .fetch();

        List<DiarySelectDto> ids = queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(good.count())
                                        .from(good)
                                        .where(good.diary.eq(diary)), "countOfGood")))
                .from(diary)
                .where(eqWriter(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy(aliasCount.desc(), diary.createDate.desc())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .groupBy(diary)
                .fetch();

        return queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id,
//                        qUser.as("writer"),
                        diary.writer,
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
//                        good.count().coalesce(0L).as("countOfGood")
                        ))
                .from(diary)
//                .leftJoin(diary.writer, qUser)
//                .leftJoin(good).on(good.diary.eq(diary))
                .where(diary.id.in(ids.stream().map(DiarySelectDto::getId).collect(Collectors.toList())))
//                .where(eqWriter(user))
//                .orderBy(aliasCount.desc(), diary.createDate.desc())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
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

    private List<OrderSpecifier> getOrderSpecifier(Pageable pageable){
        NumberPath<Long> aliasCount = Expressions.numberPath(Long.class, "countOfGood");
        List<OrderSpecifier> orders = new ArrayList<>();
        if(pageable.getSort() != null){
            for(Sort.Order order : pageable.getSort()){
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()){
                    case "createDate":
                        OrderSpecifier<?> createDate = new OrderSpecifier(direction, diary.createDate);
                        orders.add(createDate);
                        break;
                    case "good":
                        OrderSpecifier<?> goodCount = new OrderSpecifier(direction, aliasCount);
                        orders.add(goodCount);
                        OrderSpecifier<?> createDate2 = new OrderSpecifier(direction, diary.createDate);
                        orders.add(createDate2);
                        break;
                }
            }
        }
        return orders;
    }

}
