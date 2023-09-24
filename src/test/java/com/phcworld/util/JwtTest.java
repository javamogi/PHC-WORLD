package com.phcworld.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Base64;

@Slf4j
public class JwtTest {

    @Test
    public void test(){
        String secretKeyPlain = "spring-security-jwt-phc-word";
        // 키를 Base64 인코딩
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        log.info("key : {}", keyBase64Encoded);
    }
}
