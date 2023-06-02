package com.study.ebsoft.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FileDTO {
    private long fileIdx;
    private String savedFileName;
    private String originalFileName;
    private int fileSize;
    private long boardIdx;

    public FileDTO() {
    }

    @Builder
    public FileDTO(long fileIdx, String savedFileName, String originalFileName, int fileSize, long boardIdx) {
        this.fileIdx = fileIdx;
        this.savedFileName = savedFileName;
        this.originalFileName = originalFileName;
        this.fileSize = fileSize;
        this.boardIdx = boardIdx;
    }
}
