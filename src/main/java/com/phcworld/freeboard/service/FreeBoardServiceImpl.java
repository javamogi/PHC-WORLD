package com.phcworld.freeboard.service;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.FreeBoardAnswerRepository;
import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.exception.model.DeletedEntityException;
import com.phcworld.exception.model.FreeBoardNotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class FreeBoardServiceImpl implements FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final FreeBoardAnswerRepository freeBoardAnswerRepository;

    @Override
    public FreeBoard register(FreeBoardRequest request, User user) {
        FreeBoard freeBoard = FreeBoard.from(request, user, localDateTimeHolder);
        return freeBoardRepository.save(freeBoard);
    }

    @Override
    public List<FreeBoard> findAllList() {
        return freeBoardRepository.findAll();
    }

    @Override
    public FreeBoard addReadCount(Long freeBoardId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, 10, Sort.by("updateDate").ascending());
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
                .orElseThrow(FreeBoardNotFoundException::new);
        Page<FreeBoardAnswer> answers = freeBoardAnswerRepository.findByFreeBoardId(freeBoardId, pageable);
        freeBoard = freeBoard.addCount(answers);
        freeBoardRepository.save(freeBoard);
        return freeBoard;
    }

    @Override
    public FreeBoard getFreeBoard(Long id, UserEntity loginUser) {
        FreeBoard freeBoard = freeBoardRepository.findById(id)
                .orElseThrow(FreeBoardNotFoundException::new);
        if(!freeBoard.matchWriter(loginUser)){
            throw new NotMatchUserException();
        }
        return freeBoard;
    }

    @Override
    public FreeBoard update(FreeBoardUpdateRequest request, UserEntity loginUser) {
        FreeBoard freeBoard = getFreeBoard(request.getId(), loginUser);
        freeBoard = freeBoard.update(request, localDateTimeHolder);
        return freeBoardRepository.save(freeBoard);
    }

    @Override
    public FreeBoard delete(Long id, UserEntity loginUser) {
        FreeBoard freeBoard = freeBoardRepository.findById(id)
                .orElseThrow(FreeBoardNotFoundException::new);
        if(freeBoard.isDeleted()){
            throw new DeletedEntityException();
        }
        if(!freeBoard.matchWriter(loginUser) && !freeBoard.matchAdminAuthority(loginUser)){
            throw new NotMatchUserException();
        }
        freeBoard = freeBoard.delete(localDateTimeHolder);
        return freeBoardRepository.save(freeBoard);
    }

    @Override
    public List<FreeBoard> getFreeBoardListByUserId(Long userId, Long freeBoardId) {
        return freeBoardRepository.findByUser(userId, freeBoardId);
    }
}
