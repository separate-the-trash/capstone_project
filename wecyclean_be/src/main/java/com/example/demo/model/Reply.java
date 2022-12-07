package com.example.demo.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyno;

    private String userid;
    private String content;

    @CreationTimestamp
    private LocalDateTime regdate;

    @ManyToOne
    @JoinColumn(name="board_postid", referencedColumnName = "postid")
    private Board board;
}
