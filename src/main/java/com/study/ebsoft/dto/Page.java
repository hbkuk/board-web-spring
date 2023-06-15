package com.study.ebsoft.dto;

import lombok.Data;

@Data
public class Page {
    private static final int PAGE_LIMIT_NO = 10;

    private final int pagingStart;

    public Page(int pageNo) {
        this.pagingStart = (pageNo - 1) * PAGE_LIMIT_NO;
    }
}
