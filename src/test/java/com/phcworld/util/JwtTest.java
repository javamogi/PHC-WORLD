package com.phcworld.util;

import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.jwt.TokenProvider;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.repository.user.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JwtTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${jwt.secret}")
    private String secretKey;

    @Test
    public void 비밀키_암호화(){
        String secretKeyPlain = "spring-security-jwt-phc-world-secret-key";
        // 키를 Base64 인코딩
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        log.info("key : {}", keyBase64Encoded);
        log.info("secret key : {}", secretKey);
        assertThat(keyBase64Encoded).isEqualTo(secretKey);
    }

    @Test
    public void 토큰_생성(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(user, now);
        log.info("access token : {}", accessToken);
    }

    @Test
    public void 토큰_검증_성공(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(user, now);
        boolean result = tokenProvider.validateToken(accessToken);
        assertThat(result).isTrue();
    }

    @Test(expected = CustomException.class)
    public void 토큰_검증_잘못된_토큰(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        long now = (new Date()).getTime();
        String accessToken = tokenProvider.generateAccessToken(user, now);
        accessToken = accessToken.replace(".", "");
        tokenProvider.validateToken(accessToken);
    }

    @Test(expected = CustomException.class)
    public void 토큰_검증_만료된_토큰(){
        String accessToken = Jwts.builder()
                .setExpiration(new Date(new Date().getTime()))
                .compact();

        tokenProvider.validateToken(accessToken);
    }

    @Test
    public void 토큰_응답_dto_생성(){
        User user = userRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        TokenDto dto = tokenProvider.generateTokenDto(user);
        log.info("token : {}", dto);
    }
}
