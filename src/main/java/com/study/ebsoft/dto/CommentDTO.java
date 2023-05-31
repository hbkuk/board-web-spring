package com.study.ebsoft.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDTO {
    private Long commentIdx;
    private String writer;
    private String password;
    private String content;
    private LocalDateTime regDate;
    private long boardIdx;

    @Override
    public String toString() {
        return "CommentDTO{" +
                "commentIdx=" + commentIdx +
                ", writer='" + writer + '\'' +
                ", password='" + password + '\'' +
                ", content='" + content + '\'' +
                ", regDate=" + regDate +
                ", boardIdx=" + boardIdx +
                '}';
    }
}