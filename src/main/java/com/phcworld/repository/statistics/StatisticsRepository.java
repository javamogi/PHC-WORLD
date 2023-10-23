package com.phcworld.repository.statistics;

import com.phcworld.domain.board.QDiary;
import com.phcworld.domain.board.QFreeBoard;
import com.phcworld.domain.statistics.StatisticsDto;
import com.phcworld.domain.statistics.UserStatisticsDto;
import com.phcworld.domain.user.QUser;
import com.phcworld.domain.user.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
    private final JPAQueryFactory queryFactory;
    QDiary diary = QDiary.diary;

    QUser user = QUser.user;

    QFreeBoard freeBoard = QFreeBoard.freeBoard;

    public List<StatisticsDto> findRegisterUserStatistics(LocalDate startDate, LocalDate endDate){
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23,59,59);
        // mysql
//        DateTemplate<String> formattedDate = Expressions.dateTemplate(
//                String.class,
//                "DATE_FORMAT({0}, {1})",
//                user.createDate,
//                ConstantImpl.create("%Y-%m-%d")
//        );
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                user.createDate,
//                ConstantImpl.create("%Y-%m-%d")
                "%Y-%m-%d"
        );

//        StringTemplate formattedDate = Expressions.stringTemplate(
//                "FORMATDATETIME({0}, {1})",
//                user.createDate,
//                "yyyy-MM-dd"
//        );


        return queryFactory.select(Projections.fields(StatisticsDto.class,
                        user.count().as("count"),
                        formattedDate.as("date")))
                .from(user)
                .where(user.createDate.between(start, end))
//                        user.createDate.goe(start)
//                        .and(user.createDate.loe(end)))
                .groupBy(formattedDate)
                .fetch();
    }

    public List<StatisticsDto> findPostDiaryStatistics(LocalDate startDate, LocalDate endDate){
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23,59,59);
        // mysql
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                diary.createDate,
                "%Y-%m"
        );

        return queryFactory.select(Projections.fields(StatisticsDto.class,
                        diary.count().as("count"),
                        formattedDate.as("date")))
                .from(diary)
                .where(diary.createDate.between(start, end))
                .groupBy(formattedDate)
                .fetch();
    }

    public List<UserStatisticsDto> findUserPostRegisterStatistics(User searchUser){

        return queryFactory.select(Projections.fields(UserStatisticsDto.class,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(freeBoard.count())
                                        .from(freeBoard)
                                        .where(freeBoard.writer.eq(user)), "freeBoardPostCount"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(diary.count())
                                        .from(diary)
                                        .where(diary.writer.eq(user)), "diaryPostCount"),
                        user.name.as("userId")))
                .from(user)
                .where(eqUser(searchUser))
                .fetch();
    }

    private Predicate eqUser(User searchUser) {
        if(Objects.isNull(searchUser)){
            return null;
        }
        return user.eq(searchUser);
    }

    public List<UserStatisticsDto> findUserPostRegisterStatistics2(){

        return queryFactory.select(Projections.fields(UserStatisticsDto.class,
                        user.name.as("userId"),
                        freeBoard.count().as("freeBoardPostCount"),
                        diary.count().as("diaryPostCount")))
                .from(user)
                .leftJoin(freeBoard).on(freeBoard.writer.eq(user))
                .leftJoin(diary).on(diary.writer.eq(user))
                .groupBy(user.name)
                .fetch();
    }
}
