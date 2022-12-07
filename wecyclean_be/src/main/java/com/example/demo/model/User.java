package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, unique = true)
    private String username;

    @Column(length = 200)
    private String password;

    @Column(length = 100)
    private String role;

    @Column(length = 200)
    private int point;

    public void add50Point() {
        this.point += 50;
    }

    public void usePoint(int reward) {
        point -= reward;
    }

}
