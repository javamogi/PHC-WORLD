package com.phcworld.repository.user;

import com.phcworld.domain.statistics.UserStatistics;
import com.phcworld.domain.user.QUser;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;

    @Override
    public List<UserStatistics> findRegisterUserStatistics(LocalDate startDate, LocalDate endDate){
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


        return queryFactory.select(Projections.fields(UserStatistics.class,
                        user.count().as("count"),
                        formattedDate.as("date")))
                .from(user)
                .where(user.createDate.between(start, end))
//                        user.createDate.goe(start)
//                        .and(user.createDate.loe(end)))
                .groupBy(formattedDate)
                .fetch();
    }
}
