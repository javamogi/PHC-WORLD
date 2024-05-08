package com.phcworld.freeboard.controller;

import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/freeboards")
@RequiredArgsConstructor
@Builder
public class FreeBoardApiController {

    private final FreeBoardService freeBoardService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<FreeBoardResponse>> getFreeBoardsByUser(@PathVariable Long userId,
                                                                       @RequestParam(required = false) Long freeBoardId){
        List<FreeBoardResponse> list = freeBoardService.getFreeBoardListByUserId(userId, freeBoardId)
                .stream()
                .map(FreeBoardResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(200)
                .body(list);
    }
}
