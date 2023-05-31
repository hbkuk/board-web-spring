package com.study.ebsoft.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileDTO {
    private long fileIdx;
    private String savedFileName;
    private String originalFileName;
    private int fileSize;
    private long BoardIdx;
}
