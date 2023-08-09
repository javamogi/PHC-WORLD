package com.phcworld.repository.board;

import com.phcworld.domain.answer.QFreeBoardAnswer;
import com.phcworld.domain.board.QFreeBoard;
import com.phcworld.domain.user.QUser;
import com.phcworld.repository.board.dto.FreeBoardSelectDto;
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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FreeBoardRepositoryCustomImpl implements FreeBoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QFreeBoard freeBoard = QFreeBoard.freeBoard;
    QUser user = QUser.user;
    QFreeBoardAnswer answer = QFreeBoardAnswer.freeBoardAnswer;

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
                        freeBoard.icon,
                        freeBoard.badge,
                        freeBoard.createDate,
                        freeBoard.updateDate,
                        freeBoard.count,
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
}
