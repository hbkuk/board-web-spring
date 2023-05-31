package com.study.model.board;

import com.study.ebsoft.model.board.ModDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("글 수정일자는")
public class ModDateTest {

    @Test
    void create() {
        ModDate modDate = new ModDate(LocalDateTime.now());
    }
}
