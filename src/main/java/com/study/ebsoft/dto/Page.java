package com.study.ebsoft.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;

/**
 * 페이지 정보를 담는 데이터 전송 객체입니다.
 */
@Data
public class Page {

    /**
     * 페이지당 레코드 수
     */
    public static final int RECORDS_PER_PAGE = 10;

    /**
     * 블록당 페이지 수
     */
    public static final int BLOCK_PER_PAGE = 10;

    /**
     * 페이지 번호
     */
    @Nullable
    @Min(value = 1, message = "페이지 번호는 1보다 큰 숫자여야 합니다")
    private Integer pageNo;

    /**
     * 레코드 시작 인덱스
     */
    @JsonProperty
    private Integer recordStartIndex;

    /**
     * 최대 페이지 수
     */
    @JsonProperty
    private Integer maxPage;

    /**
     * 시작 페이지
     */
    @JsonProperty
    private Integer startPage;

    /**
     * 종료 페이지
     */
    @JsonProperty
    private Integer endPage;

    /**
     * 페이지 번호를 기반으로 Page 객체를 생성합니다.
     *
     * @param pageNo 페이지 번호
     */
    public Page(int pageNo) {
        this.pageNo = pageNo;
        this.recordStartIndex = (pageNo - 1) * RECORDS_PER_PAGE;
    }

    /**
     * 전체 게시글 수를 기반으로 페이지 정보를 계산합니다.
     *
     * @param totalBoardCount 전체 게시글 수
     * @return 계산된 페이지 정보를 포함한 현재 객체
     */
    public Page calculatePaginationInfo(int totalBoardCount) {
        this.maxPage = updateMaxPage(totalBoardCount);
        this.startPage = updateStartPage();
        this.endPage = updateEndPage();
        return this;
    }

    /**
     * 종료 페이지를 업데이트합니다.
     *
     * @return 업데이트된 종료 페이지
     */
    private int updateEndPage() {
        int endPage = startPage + BLOCK_PER_PAGE - 1;
        if (endPage > this.maxPage) {
            endPage = maxPage;
        }
        return endPage;
    }

    /**
     * 시작 페이지를 업데이트합니다.
     *
     * @return 업데이트된 시작 페이지
     */
    private int updateStartPage() {
        return (((int) (Math.ceil((double) pageNo / BLOCK_PER_PAGE))) - 1) * BLOCK_PER_PAGE + 1;
    }

    /**
     * 최대 페이지 수를 업데이트합니다.
     *
     * @param totalBoardCount 전체 게시글 수
     * @return 업데이트된 최대 페이지 수
     */
    private int updateMaxPage(double totalBoardCount) {
        return (int) (Math.ceil(totalBoardCount / RECORDS_PER_PAGE));
    }
}
