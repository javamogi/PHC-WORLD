package com.phcworld.web.api.movie;

import com.phcworld.service.movie.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class WebClientApiController {

    private final SearchApiService searchApiService;

    @GetMapping(value = "/api/books", produces="application/json;charset=UTF-8")
    public String searchBooks(HttpServletRequest request){
        return searchApiService.getResult(request);
    }
}
