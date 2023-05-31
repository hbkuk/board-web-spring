package com.study.ebsoft.model.comment;

import java.util.Objects;

public class CommentIdx {
    private static final int MIN_COMMENTIDX_VALUE = 0;
    private long commentIdx = 0;

    public CommentIdx(long value) {
        if(value < MIN_COMMENTIDX_VALUE) {
            throw new IllegalArgumentException("글 번호는 음수일 수 없습니다.");
        }
        this.commentIdx = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CommentIdx that = (CommentIdx) o;
        return commentIdx == that.commentIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentIdx);
    }
}
