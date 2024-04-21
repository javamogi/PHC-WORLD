package com.phcworld.config;

import com.phcworld.common.aop.TimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public TimeAspect timeAspect(){
        return new TimeAspect();
    }
}
