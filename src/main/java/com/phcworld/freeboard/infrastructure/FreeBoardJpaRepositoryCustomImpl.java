package com.phcworld.freeboard.infrastructure;

import com.phcworld.answer.infrastructure.QFreeBoardAnswerEntity;
import com.phcworld.freeboard.infrastructure.dto.AnswerSelectDto;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardAndAnswersSelectDto;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import com.phcworld.user.infrastructure.QUserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class FreeBoardJpaRepositoryCustomImpl implements FreeBoardJpaRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QFreeBoardEntity freeBoard = QFreeBoardEntity.freeBoardEntity;
    QUserEntity user = QUserEntity.userEntity;
    QFreeBoardAnswerEntity answer = QFreeBoardAnswerEntity.freeBoardAnswerEntity;

    @Override
    public List<FreeBoardSelectDto> findByKeywordOrderById(String keyword, Pageable pageable){
        List<Long> ids = queryFactory
                .select(freeBoard.id)
                .from(freeBoard)
                .leftJoin(freeBoard.writer, user)
                .where(
//                        findByKeyword(keyword),
                        user.name.contains(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(freeBoard.createDate.desc())
//                .orderBy(freeBoard.id.desc())
                .fetch();

        return queryFactory
                .select(Projections.fields(FreeBoardSelectDto.class,
                        freeBoard.id.as("id"),
                        user.as("writer"),
                        freeBoard.title,
                        freeBoard.contents,
                        freeBoard.createDate,
                        freeBoard.updateDate,
                        freeBoard.count,
                        freeBoard.isDeleted,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(answer.count())
                                        .from(answer)
                                        .where(answer.writer.eq(user)), "countOfAnswer")))
                .from(freeBoard)
                .leftJoin(freeBoard.writer, user)
                .where(freeBoard.id.in(ids))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(freeBoard.id.desc())
                .orderBy(freeBoard.createDate.desc())
                .fetch();
    }

    private BooleanExpression findByKeyword(String keyword){
        if(Objects.isNull(keyword) || keyword.isEmpty()){
            return null;
        }
        return freeBoard.title.contains(keyword)
                .or(freeBoard.contents.contains(keyword));
    }

    private BooleanBuilder findContents(String keyword){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(Objects.isNull(keyword) || keyword.isEmpty()){
            return null;
        }
        return booleanBuilder.or(freeBoard.contents.contains(keyword));
    }

//    public Page<FreeBoardSelectDto> findByKeywordAndUserPage(String keyword, User user, Pageable pageable) {
//        List<FreeBoardSelectDto> content = findAllOrderByCreateDate(keyword, user, pageable);
//        Long count = getTotalCount(keyword, user);
//        return new PageImpl<>(content, pageable, count);
//    }

    @Override
    public Optional<FreeBoardAndAnswersSelectDto> findByIdAndAnswers(long id, Pageable pageable){
        QUserEntity aw = new QUserEntity("aw");
        Map<Long, FreeBoardAndAnswersSelectDto> result = queryFactory
                .selectFrom(freeBoard)
                .leftJoin(answer).on(answer.freeBoard.eq(freeBoard))
                .leftJoin(user).on(user.eq(freeBoard.writer))
                .leftJoin(aw).on(aw.eq(answer.writer))
                .where(freeBoard.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(answer.updateDate.asc())
                .transform(groupBy(freeBoard.id).as(Projections.fields(FreeBoardAndAnswersSelectDto.class,
                                        freeBoard.id,
                                        user.as("writer"),
                                        freeBoard.title,
                                        freeBoard.contents,
                                        freeBoard.createDate,
                                        freeBoard.updateDate,
                                        freeBoard.count,
                                        freeBoard.isDeleted,
                                        ExpressionUtils.as(
                                                JPAExpressions
                                                        .select(answer.count())
                                                        .from(answer)
                                                        .where(answer.freeBoard.id.eq(id)), "countOfAnswer"),
                                        list(Projections.fields(
                                                        AnswerSelectDto.class,
                                                        answer.id,
                                                answer.contents,
                                                answer.createDate,
                                                answer.updateDate,
                                                aw.as("writer")
                                                )
                                        ).as("answers"))
                                ));
        return result.values().stream().findAny();
    }

    @Override
    public List<FreeBoardSelectDto> findAllWithAnswersCount(){
        return queryFactory
                .select(Projections.fields(FreeBoardSelectDto.class,
                        freeBoard.id.as("id"),
                        user.as("writer"),
                        freeBoard.title,
                        freeBoard.contents,
                        freeBoard.createDate,
                        freeBoard.updateDate,
                        freeBoard.count,
                        freeBoard.isDeleted,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(answer.count())
                                        .from(answer)
                                        .where(answer.freeBoard.eq(freeBoard)), "countOfAnswer")))
                .from(freeBoard)
                .where(freeBoard.isDeleted.eq(false))
                .leftJoin(freeBoard.writer, user)
                .orderBy(freeBoard.createDate.desc())
                .fetch();
    }

    @Override
    public List<FreeBoardSelectDto> findByUser(Long userId, Long freeBoardId){
        return queryFactory
                .select(Projections.fields(FreeBoardSelectDto.class,
                        freeBoard.id.as("id"),
                        user.as("writer"),
                        freeBoard.title,
                        freeBoard.contents,
                        freeBoard.createDate,
                        freeBoard.updateDate,
                        freeBoard.count,
                        freeBoard.isDeleted,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(answer.count())
                                        .from(answer)
                                        .where(answer.freeBoard.eq(freeBoard)), "countOfAnswer")))
                .from(freeBoard)
                .where(freeBoard.isDeleted.eq(false),
                        eqUsers(userId),
                        ltFreeBoardId(freeBoardId))
                .leftJoin(freeBoard.writer, user)
                .orderBy(freeBoard.createDate.desc())
                .limit(10)
                .fetch();
    }

    private BooleanBuilder eqUsers(Long userId) {
        if(Objects.isNull(userId) || userId == 0) {
            return null;
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(freeBoard.writer.id.eq(userId));
    }

    private BooleanExpression ltFreeBoardId(Long freeBoardId) {
        if(Objects.isNull(freeBoardId)) {
            return null;
        }
        return freeBoard.id.lt(freeBoardId);
    }
}
