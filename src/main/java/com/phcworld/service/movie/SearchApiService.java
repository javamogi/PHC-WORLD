package com.phcworld.service.movie;

import com.phcworld.exception.model.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchApiService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    private final String MOVIE_URL = "https://openapi.naver.com/v1/search/book.json?query=";

    public String getResult(HttpServletRequest request) {
        String title = request.getParameter("title");
        String pnum = request.getParameter("pnum");
        if(pnum == null || pnum == ""){
            pnum = "1";
        }
        int pageNUM = Integer.parseInt(pnum);
        int start = (pageNUM - 1) * 10 + 1;
        try {
            String text = URLEncoder.encode(title, "UTF-8");
            String requestUrl = MOVIE_URL + text + "&start=" + start;
            return webClient
                    .get()
                    .uri(requestUrl)
                    .header("X-Naver-Client-Id", clientId)
                    .header("X-Naver-Client-Secret", clientSecret)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.debug("message : {}", e.getMessage());
            throw new CustomException("400", e.getMessage());
        }
    }

}
