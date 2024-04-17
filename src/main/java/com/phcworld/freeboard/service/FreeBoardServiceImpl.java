package com.phcworld.freeboard.service;

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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class FreeBoardServiceImpl implements FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final LocalDateTimeHolder localDateTimeHolder;

    @Override
    public FreeBoard register(FreeBoardRequest request, User user) {
        FreeBoard freeBoard = FreeBoard.from(request, user, localDateTimeHolder);
        return freeBoardRepository.save(freeBoard);
    }

    @Override
    public List<FreeBoard> findAllList() {
        return freeBoardRepository.findAll()
                .stream()
                .filter(f -> !f.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public FreeBoard addReadCount(Long freeBoardId) {
        FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId)
                .orElseThrow(FreeBoardNotFoundException::new);
        freeBoard = freeBoard.addCount();
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
}
