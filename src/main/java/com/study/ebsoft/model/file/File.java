package com.study.ebsoft.model.file;

import com.study.ebsoft.model.board.BoardIdx;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class File {
    private final FileIdx fileIdx;
    private final String savedFileName;
    private final FileOriginalName originalName;
    private final FileSize fileSize;
    private final BoardIdx boardIdx;

    public File(FileIdx fileIdx, String savedFileName, FileOriginalName originalName,
                FileSize fileSize, BoardIdx boardIdx) {
        this.fileIdx = fileIdx;
        this.savedFileName = savedFileName;
        this.originalName = originalName;
        this.fileSize = fileSize;
        this.boardIdx = boardIdx;
    }

    public File(String savedFileName, FileOriginalName originalName, FileSize fileSize) {
        this(new FileIdx(0), savedFileName, originalName, fileSize, new BoardIdx(0));
    }

    public File(Builder builder) {
        this.fileIdx = builder.FileIdx;
        this.savedFileName = builder.saveFileName;
        this.originalName = builder.originalFileName;
        this.fileSize = builder.fileSize;
        this.boardIdx = builder.boardIdx;
    }

    public static class Builder {
        private FileIdx FileIdx;
        private String saveFileName;
        private FileOriginalName originalFileName;
        private FileSize fileSize;
        private BoardIdx boardIdx;

        public Builder(){};

        public Builder fileIdx(FileIdx imageIdx) {
            this.FileIdx = imageIdx;
            return this;
        }
        public Builder saveFileName(String saveFileName) {
            this.saveFileName = saveFileName;
            return this;
        }
        public Builder originalName(FileOriginalName originalName) {
            this.originalFileName = originalName;
            return this;
        }
        public Builder fileSize(FileSize imageSize) {
            this.fileSize = imageSize;
            return this;
        }
        public Builder boardIdx(BoardIdx boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public File build() {
            if (!Stream.of(originalFileName).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new File(this);
        }
    }
}
