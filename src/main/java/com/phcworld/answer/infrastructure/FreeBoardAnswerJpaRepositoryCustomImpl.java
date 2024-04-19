package com.phcworld.answer.infrastructure;

import com.phcworld.answer.infrastructure.dto.FreeBoardAnswerSelectDto;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.user.infrastructure.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeBoardAnswerJpaRepositoryCustomImpl implements FreeBoardAnswerJpaRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    QUserEntity user = QUserEntity.userEntity;
    QFreeBoardAnswerEntity answer = QFreeBoardAnswerEntity.freeBoardAnswerEntity;


    @Override
    public Page<FreeBoardAnswerSelectDto> findByFreeBoardId(long freeBoardId, Pageable pageable) {
        List<FreeBoardAnswerSelectDto> content = findListByFreeBoardId(freeBoardId, pageable);
        Long count = findCountByFreeBoardId(freeBoardId);
        return new PageImpl<>(content, pageable, count);
    }

    private Long findCountByFreeBoardId(long freeBoardId) {
        return queryFactory
                .select(answer.count())
                .from(answer)
                .where(answer.freeBoard.id.eq(freeBoardId))
                .fetchOne();
    }

    private List<FreeBoardAnswerSelectDto> findListByFreeBoardId(long freeBoardId, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(FreeBoardAnswerSelectDto.class,
                        answer.id,
                        answer.writer,
                        answer.contents,
                        answer.updateDate))
                .from(answer)
                .join(answer.writer, user)
                .where(answer.freeBoard.id.eq(freeBoardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(answer.createDate.asc())
                .fetch();
    }
}
