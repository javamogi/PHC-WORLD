package com.phcworld.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
//			.allowedOrigins("http://www.phcworld.com");
			.allowedOrigins("http://localhost:3000");
	}

	@Bean
	public WebClient webClient() {
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
		return WebClient.builder()
				.uriBuilderFactory(factory)
				.filter(ExchangeFilterFunction.ofRequestProcessor(
						clientRequest -> {
							log.info("------------------ request ------");
							log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
							clientRequest.headers().forEach(
									(name, values) -> values.forEach(value -> log.info("{} : {}", name, value))
							);
							return Mono.just(clientRequest);
						}
				))
				.build();
	}
}
