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

    private List<DiarySelectDto> findAllListToSub(User user, Pageable pageable){

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
//                        diary.goodPushedUser.size().as("countOfGood")))
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(good.count())
                                        .from(good)
                                        .where(good.diary.eq(diary)), "countOfGood")))
                .from(diary)
                .where(eqWriter(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy(diary.goodPushedUser.size().desc(), diary.createDate.desc())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
//                .groupBy(diary)
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
//                        diary.goodPushedUser.size().as("countOfGood"),
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
                    case "good2":
                        OrderSpecifier<?> goodCount2 = new OrderSpecifier(direction, diary.goodPushedUser.size());
                        orders.add(goodCount2);
                        OrderSpecifier<?> createDate3 = new OrderSpecifier(direction, diary.createDate);
                        orders.add(createDate3);
                        break;
                    case "good3":
                        OrderSpecifier<?> columnGoodCount = new OrderSpecifier(direction, diary.countGood);
                        orders.add(columnGoodCount);
                        OrderSpecifier<?> createDate4 = new OrderSpecifier(direction, diary.createDate);
                        orders.add(createDate4);
                        break;
                }
            }
        }
        return orders;
    }

    // 다른 테이블의 개수를 정렬 기준으로 삼기 때문에 임시테이블 + 정렬은 어쩔수 없이 사용할 것이라 판단
    // 그래서 필요한 정보만 가져오는 방식을 생각함
    // 속도에서는 많이 빨라질 것이라 예상됨
    // 하지만 페이징처리에서 막힘
    // Diary에 좋아요 개수를 담당하는 컬럼을 추가할까 했지만 너무 잘 바뀌는 요소라 추가하지 않음
    private List<DiarySelectDto> findAllListTemp(User user, Pageable pageable){

        List<OrderSpecifier> orders = getOrderSpecifier(pageable);

        int pageSize = pageable.getPageSize();
        List<Long> ids = queryFactory
                .select(diary.id)
                .from(diary)
                .where(eqWriter(user),
                        diary.goodPushedUser.isNotEmpty())
                .offset(pageable.getOffset())
                .limit(pageSize)
//                .orderBy(diary.goodPushedUser.size().desc(), diary.createDate.desc())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();

        List<Long> idsList = ids;
        if(ids.size() < pageSize){
            pageSize = pageSize - ids.size();
            List<Long> ids2 = queryFactory
                    .select(diary.id)
                    .from(diary)
                    .where(eqWriter(user),
                            diary.goodPushedUser.isEmpty())
                    .offset(pageable.getOffset())
                    .limit(pageSize)
                    .orderBy(diary.createDate.desc())
                    .fetch();
            idsList.addAll(ids2);
        }

        return queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id,
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
                ))
                .from(diary)
                .where(diary.id.in(idsList))
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();
    }

    // size()를 이용한 방식
    // 서브쿼리와 같음
    // DiarySelectDto의 countOfGood 자료형을 Integer로 변경해야함
    // 서브쿼리를 사용한다는것은 같지만 속도는 0.5초가량 빠름
    private List<DiarySelectDto> findAllListGoodPushedUserSize(User user, Pageable pageable){

        List<OrderSpecifier> orders = getOrderSpecifier(pageable);

        List<DiarySelectDto> ids = queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id,
                        diary.goodPushedUser.size().as("countOfGood")))
                .from(diary)
                .where(eqWriter(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();

        return queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id,
                        diary.writer,
                        diary.title,
                        diary.thumbnail,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(answer.count())
                                        .from(answer)
                                        .where(answer.diary.eq(diary)), "countOfAnswers"),
                        diary.goodPushedUser.size().as("countOfGood"),
                        diary.updateDate,
                        diary.createDate
                ))
                .from(diary)
                .where(diary.id.in(ids.stream().map(DiarySelectDto::getId).collect(Collectors.toList())))
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();
    }

    // 좋아요 카운트 컬럼을 추가하여 index도 추가
    // 쓰기에서 이전보다 더 많은 비용이 발생할 것으로 예상
    private List<DiarySelectDto> findAllList(User user, Pageable pageable){

        List<OrderSpecifier> orders = getOrderSpecifier(pageable);

        List<DiarySelectDto> ids = queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id))
                .from(diary)
                .where(eqWriter(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();

        return queryFactory
                .select(Projections.fields(DiarySelectDto.class,
                        diary.id,
                        diary.writer,
                        diary.title,
                        diary.thumbnail,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(answer.count())
                                        .from(answer)
                                        .where(answer.diary.eq(diary)), "countOfAnswers"),
                        diary.countGood.as("countOfGood"),
                        diary.updateDate,
                        diary.createDate
                ))
                .from(diary)
                .where(diary.id.in(ids.stream().map(DiarySelectDto::getId).collect(Collectors.toList())))
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .fetch();
    }

}
