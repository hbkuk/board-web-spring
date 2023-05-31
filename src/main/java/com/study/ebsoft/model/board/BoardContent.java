package com.study.ebsoft.model.board;

import java.util.Objects;

public class BoardContent {

    private static final int MIN_CONTENT_LENGTH_VALUE = 4;
    private static final int MAX_CONTENT_LENGTH_VALUE = 1999;
    private String content;

    public BoardContent(String content) {
        if (content.length() < MIN_CONTENT_LENGTH_VALUE || content.length() > MAX_CONTENT_LENGTH_VALUE) {
            throw new IllegalArgumentException("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");
        }
        this.content = content;
    }

    /**
     * 컨텐츠 내용을 리턴함.
     * @return
     */
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BoardContent content1 = (BoardContent) o;
        return Objects.equals(content, content1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
