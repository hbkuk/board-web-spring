package com.study.dto;

import com.study.ebsoft.dto.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class PageTest {

    @DisplayName("전달받는 인자에 따라서 페이지가 시작되는 숫자가 변경된다.")
    @ParameterizedTest
    @CsvSource(value = {"1:0", "2:10", "3:20", "4:30"}, delimiter = ':')
    void create(int pageNo, int pagingStart) {
        Page page = new Page(pageNo);

        assertThat(page.getPagingStart()).isEqualTo(pagingStart);
    }
}
