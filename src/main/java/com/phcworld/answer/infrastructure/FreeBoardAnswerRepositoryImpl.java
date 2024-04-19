package com.phcworld.answer.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.dto.FreeBoardAnswerSelectDto;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FreeBoardAnswerRepositoryImpl implements FreeBoardAnswerRepository{

    private final FreeBoardAnswerJpaRepository freeBoardAnswerJpaRepository;

    @Override
    public FreeBoardAnswer save(FreeBoardAnswer freeBoardAnswer) {
        return freeBoardAnswerJpaRepository.save(FreeBoardAnswerEntity.from(freeBoardAnswer)).toModel();
    }

    @Override
    public Optional<FreeBoardAnswer> findById(long id) {
        return freeBoardAnswerJpaRepository.findById(id).map(FreeBoardAnswerEntity::toModel);
    }

    @Override
    public void deleteById(long id) {
        freeBoardAnswerJpaRepository.deleteById(id);
    }

//    @Override
//    public Page<FreeBoardAnswer> findByFreeBoard(FreeBoard freeBoard, Pageable pageable) {
//        Page<FreeBoardAnswerEntity> pages = freeBoardAnswerJpaRepository.findByFreeBoard(
//                FreeBoardEntity.from(freeBoard),
//                pageable);
//        List<FreeBoardAnswer> list = pages.getContent()
//                .stream()
//                .map(FreeBoardAnswerEntity::toModel)
//                .collect(Collectors.toList());
//        return new PageImpl<>(list, pageable, pages.getTotalElements());
//    }

    @Override
    public Page<FreeBoardAnswer> findByFreeBoardId(long freeBoardId, Pageable pageable) {
        Page<FreeBoardAnswerSelectDto> pages = freeBoardAnswerJpaRepository.findByFreeBoardId(
                freeBoardId,
                pageable);
        List<FreeBoardAnswer> list = pages.getContent()
                .stream()
                .map(FreeBoardAnswer::from)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pages.getTotalElements());
    }

}
