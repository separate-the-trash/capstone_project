package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PointDto {
    private long id;
    private int point;
    private String message;

    public PointDto(long id) {
        this.id = id;
    }

    public PointDto(String message) {
        this.message = message;
    }
}