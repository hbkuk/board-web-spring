package com.study.ebsoft.model.comment;

import java.util.Objects;

public class CommentWriter {

    private static final int MIN_WRITER_LENGTH_VALUE = 3;
    private static final int MAX_WRITER_LENGTH_VALUE = 4;
    private String writer;

    public CommentWriter(String writer) {
        if (writer.length() < MIN_WRITER_LENGTH_VALUE || writer.length() > MAX_WRITER_LENGTH_VALUE) {
            throw new IllegalArgumentException("작성자를 3글자 미만 5글자 이상을 입력할 수 없습니다.");
        }
        this.writer = writer;
    }

    public String getWriter() {
        return writer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CommentWriter that = (CommentWriter) o;
        return Objects.equals(writer, that.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(writer);
    }
}
