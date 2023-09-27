package com.phcworld.jwt;

import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import com.phcworld.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user, long now){
        String id = user.getId().toString();
        String authority = user.getAuthority().toString();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, authority)
                .claim("type", "access")
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user, long now){
        String id = user.getId().toString();
        String authority = user.getAuthority().toString();
        // Refresh Token 생성
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, authority)
                .claim("type", "refresh")
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new CustomException("401", "잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new CustomException("401", "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new CustomException("401", "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new CustomException("400", "JWT 토큰이 잘못되었습니다.");
        }
    }

    public TokenDto generateTokenDto(User user) {

        String id = user.getId().toString();
        String authorities = user.getAuthority().toString();
        String userKey = id + authorities;

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = generateAccessToken(user, now);

        // Refresh Token 생성
        String refreshToken = generateRefreshToken(user, now);

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME);
        ops.set(userKey, refreshToken, expireDuration.getSeconds());

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}