package com.study.ebsoft.dto;

import lombok.Builder;
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

    public CommentDTO() {
    }

    @Builder
    public CommentDTO(Long commentIdx, String writer, String password, String content, LocalDateTime regDate, long boardIdx) {
        this.commentIdx = commentIdx;
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.regDate = regDate;
        this.boardIdx = boardIdx;
    }
}