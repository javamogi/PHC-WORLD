package com.phcworld.freeboard.controller;

import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/freeboards")
@RequiredArgsConstructor
@Builder
public class FreeBoardApiController {

    private final FreeBoardService freeBoardService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<FreeBoardResponse>> getFreeBoardsByUser(@PathVariable Long userId){
        List<FreeBoardResponse> list = freeBoardService.getFreeBoardListByUserId(userId)
                .stream()
                .map(FreeBoardResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(200)
                .body(list);
    }
}
