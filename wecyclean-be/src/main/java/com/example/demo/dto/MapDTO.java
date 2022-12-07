package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MapDTO {
    private String address;
    private double x;
    private double y;

    @Builder
    public MapDTO(String address, double x, double y) {
        this.address = address;
        this.x = x;
        this.y = y;
    }
}