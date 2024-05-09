package com.phcworld.answer.infrastructure;

import com.phcworld.answer.infrastructure.dto.FreeBoardAnswerSelectDto;
import com.phcworld.freeboard.infrastructure.QFreeBoardEntity;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.user.infrastructure.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
public class FreeBoardAnswerJpaRepositoryCustomImpl implements FreeBoardAnswerJpaRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    QUserEntity user = QUserEntity.userEntity;
    QFreeBoardAnswerEntity answer = QFreeBoardAnswerEntity.freeBoardAnswerEntity;

    QFreeBoardEntity freeBoard = QFreeBoardEntity.freeBoardEntity;


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

    @Override
    public List<FreeBoardAnswerSelectDto> findByUserNonOffset(Long userId, Long answerId) {
        return queryFactory
                .select(Projections.fields(FreeBoardAnswerSelectDto.class,
                        answer.id,
                        answer.contents,
                        answer.updateDate,
                        answer.freeBoard))
                .from(answer)
                .join(answer.freeBoard, freeBoard)
                .where(answer.writer.id.eq(userId),
                        ltAnswerId(answerId))
                .limit(10)
                .orderBy(answer.id.desc())
                .fetch();
    }

    private BooleanExpression ltAnswerId(Long answerId) {
        if(Objects.isNull(answerId)) {
            return null;
        }
        return answer.id.lt(answerId);
    }
}
