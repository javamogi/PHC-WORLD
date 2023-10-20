package com.phcworld.repository.statistics;

import com.phcworld.domain.board.QDiary;
import com.phcworld.domain.statistics.StatisticsDto;
import com.querydsl.core.types.Projections;
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
public class StatisticsRepository {
    private final JPAQueryFactory queryFactory;
    QDiary diary = QDiary.diary;

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
}
