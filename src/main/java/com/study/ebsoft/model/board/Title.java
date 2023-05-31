package com.study.ebsoft.model.board;

import java.util.Objects;

public class Title {
    private static final int MIN_TITLE_LENGTH_VALUE = 4;
    private static final int MAX_TITLE_LENGTH_VALUE = 99;
    private static final int MIN_STANDARD_LONG_TITLE_LENGTH = 80;
    private String title;

    public Title(String title) {
        if (title.length() < MIN_TITLE_LENGTH_VALUE || title.length() > MAX_TITLE_LENGTH_VALUE) {
            throw new IllegalArgumentException("제목은 4글자 미만, 99글자 이상을 입력할 수 없습니다.");
        }
        this.title = title;
    }

    public boolean isLongTitle() {
        return this.title.length() >= MIN_STANDARD_LONG_TITLE_LENGTH;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Title title1 = (Title) o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
