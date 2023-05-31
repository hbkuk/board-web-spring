package com.study.ebsoft.model.board;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Board {
    private final BoardIdx boardIdx;
    private final CategoryIdx categoryIdx;
    private final Title title;
    private final BoardWriter writer;
    private final BoardContent content;
    private final Password password;
    private final Hit hit;
    private final RegDate regDate;
    private final ModDate modDate;

    public Board(BoardIdx boardIdx, CategoryIdx categoryIdx, Title title, BoardWriter writer,
                 BoardContent content, Password password, Hit hit, RegDate regDate, ModDate modDate) {
        this.boardIdx = boardIdx;
        this.categoryIdx = categoryIdx;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.password = password;
        this.hit = hit;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    public Board(CategoryIdx categoryIdx, Title title, BoardWriter writer,
                 BoardContent content, Password password) {
        this(new BoardIdx(0), categoryIdx, title, writer, content, password, new Hit(0), new RegDate(LocalDateTime.now()), null);
    }

    public Board(Builder builder) {
        this.boardIdx = builder.boardIdx;
        this.categoryIdx = builder.categoryIdx;
        this.title = builder.title;
        this.writer = builder.writer;
        this.content = builder.content;
        this.password = builder.password;
        this.hit = new Hit(0);
        this.regDate = new RegDate(LocalDateTime.now());
        this.modDate = null;
    }

    public static class Builder {

        private BoardIdx boardIdx;
        private CategoryIdx categoryIdx;
        private Title title;
        private BoardWriter writer;
        private BoardContent content;
        private Password password;
        private Hit hit;
        private RegDate regDate;
        private ModDate modDate;

        public Builder() {
        }

        public Builder categoryIdx(CategoryIdx category) {
            this.categoryIdx = category;
            return this;
        }

        public Builder title(Title title) {
            this.title = title;
            return this;
        }

        public Builder writer(BoardWriter writer) {
            this.writer = writer;
            return this;
        }

        public Builder content(BoardContent content) {
            this.content = content;
            return this;
        }

        public Builder password(Password password) {
            this.password = password;
            return this;
        }

        public Builder boardIdx(BoardIdx boardIdx) {
            this.boardIdx = boardIdx;
            return this;
        }

        public Builder hit(Hit hit) {
            this.hit = hit;
            return this;
        }

        public Builder regDate(RegDate regDate) {
            this.regDate = regDate;
            return this;
        }

        public Builder modDate(ModDate modDate) {
            this.modDate = modDate;
            return this;
        }

        public Board build() {
            if ( !Stream.of(categoryIdx, title, writer, content, password).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new Board(this);
        }
    }
}
