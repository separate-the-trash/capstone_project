package com.example.demo.controller;

import com.example.demo.Service.PointService;
import com.example.demo.dto.PointDto;
import com.example.demo.dto.RewardDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;

    // HTTP: GET / POST / PUT / DELETE / FETCH ...
    @PostMapping("/reward/addpoint/{id}")
    public PointDto addPoint(@PathVariable Long id) {
        return pointService.addPoint(new PointDto(id));
    }

    @PostMapping("/reward/usepoint/{id}")
    public ResponseEntity<Object> usePoint(@PathVariable Long id, @RequestBody RewardDto reward) {
        try {
            return ResponseEntity.ok(pointService.usePoint(id, reward));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error("포인트가 부족합니다"));
        }
    }

    @AllArgsConstructor
    @Getter
    private static class Error {
        private String message;
    }

}