package com.phcworld.freeboard.controller;

import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.mock.FakeModel;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FreeBoardApiControllerTest {

    @Test
    public void 회원의_고유값_id로_회원의_게시글_목록을_가져올_수_있다(){
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .countOfAnswer(1L)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        long userId = user.getId();

        // when
        ResponseEntity<List<FreeBoardResponse>> result =
                testContainer.freeBoardApiController.getFreeBoardsByUser(userId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody()).hasSize(1);
    }

}