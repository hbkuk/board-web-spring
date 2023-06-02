package com.study.ebsoft.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long commentIdx;
    private String writer;
    private String password;
    private String content;
    private LocalDateTime regDate;
    private long boardIdx;

}