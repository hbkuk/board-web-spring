package com.study.ebsoft.dto;

import lombok.Data;

@Data
public class Page {
    public static final int RECORDS_PER_PAGE = 10;   // 페이지당 레코드 수
    public static final int BLOCK_PER_PAGE = 10;     // 블록당 페이지 수

    private final int pageNo;                       // 현재 페이지
    private final int recordStartIndex;

    private int maxPage;

    private int startPage;

    private int endPage;

    public Page(int pageNo) {
        this.pageNo = pageNo;
        this.recordStartIndex = (pageNo - 1) * RECORDS_PER_PAGE;
    }

    public Page calculatePaginationInfo(int totalBoardCount) {
        this.maxPage = updateMaxPage(totalBoardCount);
        this.startPage = updateStartPage();
        this.endPage = updateEndPage();
        return this;
    }

    private int updateEndPage() {
        int endPage = startPage + BLOCK_PER_PAGE - 1;
        if (endPage > this.maxPage) {
            endPage = maxPage;
        }
        return endPage;
    }

    private int updateStartPage() {
        return (((int) (Math.ceil((double) pageNo / BLOCK_PER_PAGE))) - 1) * BLOCK_PER_PAGE + 1;
    }

    private int updateMaxPage(double totalBoardCount) {
        return (int) (Math.ceil(totalBoardCount / RECORDS_PER_PAGE));
    }

    public int getMaxPage() {
        return this.maxPage;
    }

    public int getStartPage() {
        return this.startPage;
    }

    public int getEndPage() {
        return this.endPage;
    }
}
