package com.study.ebsoft.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private int categoryIdx;
    private String category;

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryIdx=" + categoryIdx +
                ", category='" + category + '\'' +
                '}';
    }
}
