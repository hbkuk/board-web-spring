package com.study.ebsoft.model.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ModDate {

    private LocalDateTime localDatetime = LocalDateTime.now();

    public ModDate(LocalDateTime localDateTime) {
        this.localDatetime = localDateTime;
    }
}
