package com.phcworld.jwt;

import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3;  // 3일
//    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 10;  // 10초

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
    //            throw new NotMatchUserException();
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
        ops.set(userKey, refreshToken, expireDuration.getSeconds(), TimeUnit.SECONDS);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto generateTokenDto(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String id = userDetails.getUsername();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String userKey = id + authorities;

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = generateAccessToken(authentication, now);

        // Refresh Token 생성
        String refreshToken = generateRefreshToken(authentication, now);

//        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        Duration expireDuration = Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME);
//        ops.set(userKey, refreshToken, expireDuration.getSeconds());

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        String type = (String) claims.get("type");

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new CustomException("401", "권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);

        if(type.equals("refresh")){
            String id = claims.getSubject();
            String authority = (String) claims.get(AUTHORITIES_KEY);
            String userKey = id + authority;
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            String refreshToken = ops.get(userKey);

            if(refreshToken == null) {
                throw new CustomException("400", "로그아웃 된 사용자입니다.");
            }
            if(accessToken.equals(refreshToken)){
                throw new CustomException("400", "잘못된 토큰입니다.");
            }
            long now = (new Date()).getTime();

            List<ObjectError> list = new ArrayList<>();
            list.add(new ObjectError("message", "새로운 토큰 발행"));
            list.add(new ObjectError("accessToken", generateAccessToken(authentication, now)));
            throw new CustomException("420", list);
        }
        return authentication;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private String generateAccessToken(Authentication authentication, long now){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String id = userDetails.getUsername();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", "access")
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication, long now){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String id = userDetails.getUsername();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Refresh Token 생성
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", "refresh")
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}