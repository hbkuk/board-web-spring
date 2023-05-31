package com.study.ebsoft.model.comment;

import java.util.Objects;

public class CommentContent {
    private static final int MIN_CONTENT_LENGTH_VALUE = 4;
    private static final int MAX_CONTENT_LENGTH_VALUE = 999;
    private String commentContent;
    public CommentContent(String content) {
        if (content.length() < MIN_CONTENT_LENGTH_VALUE || content.length() > MAX_CONTENT_LENGTH_VALUE) {
            throw new IllegalArgumentException("내용은 4글자 미만 1000글자를 초과할 수 없습니다.");
        }
        this.commentContent = content;
    }

    public String getCommentContent() {
        return commentContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CommentContent content = (CommentContent) o;
        return Objects.equals(commentContent, content.commentContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentContent);
    }
}
