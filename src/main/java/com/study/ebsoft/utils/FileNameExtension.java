package com.study.ebsoft.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 파일 확장자를 나타내는 ENUM
 */
public enum FileNameExtension {
    PNG, JPEG, BMP, GIF, JPG, DOCS;

    /**
     * 주어진 확장자가 ENUM에 포함되어 있다면 true, 그렇지 않다면 false
     *
     * @param extension 확인할 확장자
     * @return 포함 여부
     */
    public static boolean contains(String extension) {
        List<String> extensions = Arrays.asList(values()).stream().map(Enum::name).collect(Collectors.toList());
        return extensions.contains(extension);
    }
}
