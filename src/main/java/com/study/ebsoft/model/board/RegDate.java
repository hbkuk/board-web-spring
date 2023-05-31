package com.study.ebsoft.model.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegDate {

    private LocalDateTime localDatetime = LocalDateTime.now();

    public RegDate(LocalDateTime dateTime) {
        this.localDatetime = dateTime;
    }
}
