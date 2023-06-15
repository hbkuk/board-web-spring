package com.study.dto;

import com.study.ebsoft.dto.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class PageTest {

    @DisplayName("전달받는 인자에 따라서 페이지가 시작되는 숫자가 변경된다.")
    @ParameterizedTest
    @CsvSource(value = {"1:0", "2:10", "3:20", "4:30"}, delimiter = ':')
    void create(int pageNo, int pagingStart) {
        Page page = new Page(pageNo);

        assertThat(page.getRecordStartIndex()).isEqualTo(pagingStart);
    }

    @Test
    @DisplayName("현재 페이지 1, 전체 글 갯수 100")
    void update_page_when_1_100() {
        // given
        int pageNo = 1;   // 현재 페이지
        int totalBoardCount = 100;   // 전체 글 갯수

        // when
        Page page = new Page(pageNo);
        page.calculatePaginationInfo(totalBoardCount);

        // then
        assertThat(page.getMaxPage()).isEqualTo(10);
        assertThat(page.getStartPage()).isEqualTo(1);
        assertThat(page.getEndPage()).isEqualTo(10);
    }

    @Test
    @DisplayName("현재 페이지 2, 전체 글 갯수 50")
    void update_page_when_2_50() {
        // given
        int pageNo = 2;   // 현재 페이지
        int totalBoardCount = 50;   // 전체 글 갯수

        // when
        Page page = new Page(pageNo);
        page.calculatePaginationInfo(totalBoardCount);

        // then
        assertThat(page.getMaxPage()).isEqualTo(5);
        assertThat(page.getStartPage()).isEqualTo(1);
        assertThat(page.getEndPage()).isEqualTo(5);
    }
}
