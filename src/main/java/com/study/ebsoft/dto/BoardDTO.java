package com.study.ebsoft.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardDTO {
    private long boardIdx;
    private int categoryIdx;
    private String category;
    private String title;
    private String writer;
    private String content;
    private String password;
    private int hit;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private boolean hasFile;
    private List<CommentDTO> comments;
    private List<FileDTO> files;

    public BoardDTO() {
    }

    @Builder
    public BoardDTO(long boardIdx, int categoryIdx, String category, String title, String writer, String content, String password, int hit, LocalDateTime regDate, LocalDateTime modDate, boolean hasFile, List<CommentDTO> comments, List<FileDTO> files) {
        this.boardIdx = boardIdx;
        this.categoryIdx = categoryIdx;
        this.category = category;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.password = password;
        this.hit = hit;
        this.regDate = regDate;
        this.modDate = modDate;
        this.hasFile = hasFile;
        this.comments = comments;
        this.files = files;
    }
}
