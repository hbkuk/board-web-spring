package com.study.ebsoft.model.board;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CategoryIdx {
    private static final int MIN_CATEGORY_IDX_VALUE = 0;
    private int categoryIdx = 0;

    public CategoryIdx(int value) {
        if(value < MIN_CATEGORY_IDX_VALUE) {
            throw new IllegalArgumentException("카테고리 번호는 음수일 수 없습니다.");
        }
        this.categoryIdx = value;
    }

    public int getCategoryIdx() {
        return categoryIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CategoryIdx boardId1 = (CategoryIdx) o;
        return categoryIdx == boardId1.categoryIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryIdx);
    }
}
