package com.phcworld.freeboard.domain;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.freeboard.infrastructure.dto.AnswerSelectDto;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardAndAnswersSelectDto;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FreeBoard {
    private Long id;

    private User writer;

    private String title;

    private String contents;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private int count;
    private boolean isDeleted;

    private List<FreeBoardAnswer> freeBoardAnswers;

    private Integer pageNum;


    public static FreeBoard from(FreeBoardRequest request, User user, LocalDateTimeHolder localDateTimeHolder) {
        return FreeBoard.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .writer(user)
                .createDate(localDateTimeHolder.now())
                .updateDate(localDateTimeHolder.now())
                .build();
    }

    public static FreeBoard from(FreeBoardAndAnswersSelectDto dto) {
        return FreeBoard.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .writer(dto.getWriter().toModel())
                .count(dto.getCount())
                .createDate(dto.getCreateDate())
                .updateDate(dto.getUpdateDate())
                .freeBoardAnswers(dto.getAnswers()
                        .stream()
                        .map(AnswerSelectDto::toModel)
                        .collect(Collectors.toList()))
                .isDeleted(dto.getIsDeleted())
                .build();
    }

    public String getFormattedCreateDate() {
        return LocalDateTimeUtils.getTime(createDate);
    }

    public String getCountOfAnswerString() {
        if (freeBoardAnswers == null || freeBoardAnswers.isEmpty()) {
            return "";
        }
        return "[" + freeBoardAnswers.size() + "]";
    }

    public FreeBoard addCount(int pageNum) {
        return FreeBoard.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .writer(writer)
                .count(count + 1)
                .createDate(createDate)
                .updateDate(updateDate)
                .freeBoardAnswers(freeBoardAnswers)
                .isDeleted(isDeleted)
                .pageNum(pageNum)
                .build();
    }

    public boolean matchWriter(UserEntity loginUser) {
        return writer.getId().equals(loginUser.getId()) && writer.getAuthority() == loginUser.getAuthority();
    }

    public FreeBoard update(FreeBoardUpdateRequest request, LocalDateTimeHolder localDateTimeHolder) {
        return FreeBoard.builder()
                .id(id)
                .title(title)
                .contents(request.getContents())
                .writer(writer)
                .count(count)
                .isDeleted(isDeleted)
                .createDate(createDate)
                .updateDate(localDateTimeHolder.now())
                .build();
    }

    public boolean matchAdminAuthority(UserEntity loginUser) {
        return loginUser.getAuthority() == Authority.ROLE_ADMIN;
    }

    public FreeBoard delete(LocalDateTimeHolder localDateTimeHolder) {
        return FreeBoard.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .writer(writer)
                .count(count)
                .isDeleted(true)
                .createDate(createDate)
                .updateDate(localDateTimeHolder.now())
                .build();
    }
}
