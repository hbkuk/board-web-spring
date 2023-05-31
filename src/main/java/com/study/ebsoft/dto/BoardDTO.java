package com.study.ebsoft.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "BoardDTO{" +
                "boardIdx=" + boardIdx +
                ", categoryIdx=" + categoryIdx +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                ", password='" + password + '\'' +
                ", hit=" + hit +
                ", regDate=" + regDate +
                ", modDate=" + modDate +
                ", hasFile=" + hasFile +
                ", comments=" + comments +
                ", files=" + files +
                '}';
    }
}
