package com.study.ebsoft.model.comment;

import com.study.ebsoft.model.board.BoardIdx;
import com.study.ebsoft.model.board.Password;
import com.study.ebsoft.model.board.RegDate;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class Comment {
    private final CommentIdx commentIdx;
    private final CommentWriter writer;
    private final Password password;
    private final CommentContent content;
    private final RegDate regDate;
    private final BoardIdx boardIdx;

    public Comment(CommentIdx commentIdx, CommentWriter writer, Password password,
                   CommentContent content, RegDate regDate, BoardIdx boardIdx) {
        this.commentIdx = commentIdx;
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.regDate = regDate;
        this.boardIdx = boardIdx;
    }

    public Comment(CommentWriter writer, Password password,
                   CommentContent content, BoardIdx boardIdx) {
        this(new CommentIdx(0), writer, password, content, new RegDate(LocalDateTime.now()), boardIdx);
    }

    public Comment(Builder builder) {
        this.commentIdx = new CommentIdx(0);
        this.writer = builder.writer;
        this.password = builder.password;
        this.content = builder.content;
        this.regDate = new RegDate(LocalDateTime.now());
        this.boardIdx = builder.boardIdx;
    }

    public static class Builder {

        private CommentIdx commentIdx;

        private CommentWriter writer;
        private Password password;
        private CommentContent content;
        private RegDate regDate;
        private BoardIdx boardIdx;

        public Builder() {
        }

        public Builder commentIdx(CommentIdx commentIdx) {
            this.commentIdx = commentIdx;
            return this;
        }

        public Builder writer(CommentWriter Writer) {
            this.writer = Writer;
            return this;
        }
        public Builder password(Password Password) {
            this.password = Password;
            return this;
        }
        public Builder content(CommentContent Content) {
            this.content = Content;
            return this;
        }
        public Builder regDate(RegDate RegDate) {
            this.regDate = RegDate;
            return this;
        }
        public Builder boardIdx(BoardIdx BoardIdx) {
            this.boardIdx = BoardIdx;
            return this;
        }
        public Comment build() {
            if (!Stream.of(writer, password, content, boardIdx).allMatch(Objects::nonNull)) {
                throw new IllegalArgumentException("필수값이 입력되지 않았습니다.");
            }
            return new Comment(this);
        }

    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentIdx=" + commentIdx +
                ", writer=" + writer +
                ", password=" + password +
                ", content=" + content +
                ", regDate=" + regDate +
                ", boardIdx=" + boardIdx +
                '}';
    }
}