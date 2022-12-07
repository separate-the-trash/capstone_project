package com.example.demo.model;

import com.example.demo.dto.MapDTO;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String address;
    private double x;
    private double y;

    public Map(MapDTO mapDto) {
        this.address = mapDto.getAddress();
        this.x = mapDto.getX();
        this.y = mapDto.getY();
    }

}