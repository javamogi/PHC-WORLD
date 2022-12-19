package com.phcworld.repository.board;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.QDiary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryCustomImpl implements DiaryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QDiary diary = QDiary.diary;

    private List<Diary> findAllList(Pageable pageable){
        return queryFactory
                .select(diary)
                .from(diary)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(diary.createDate.desc())
                .fetch();
    }

    private Long getAllDiaryCount(){
        return queryFactory
                .select(diary.count())
                .from(diary)
                .fetchOne();
    }

    @Override
    public Page<Diary> findAllPage(Pageable pageable){
        List<Diary> content = findAllList(pageable);
        Long count = getAllDiaryCount();
        return new PageImpl<>(content, pageable, count);
    }
}
