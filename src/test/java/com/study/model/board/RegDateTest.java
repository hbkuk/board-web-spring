package com.study.model.board;

import com.study.ebsoft.model.board.RegDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("글 등록일자는")
public class RegDateTest {

    @Test
    void create() {
        RegDate regDate = new RegDate(LocalDateTime.now());
    }
}
