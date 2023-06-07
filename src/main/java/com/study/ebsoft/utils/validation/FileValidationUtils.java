package com.study.ebsoft.utils.validation;

import com.study.ebsoft.domain.File;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileValidationUtils {

    private static final int MIN_NUMBER_VALUE = 0;

    private static final int MIN_FILE_NAME_VALUE = 5;
    private static final int MAX_FILE_NAME_VALUE = 255;

    private static final int MIN_FILE_SIZE = 1;
    private static final int MAX_FILE_SIZE = 10_485_760;

    private static final String IMAGE_NAME_EXTENSION__REGEX = "\\.(\\w+)$";
    private static final Pattern EXTENSION_PATTERN_COMPILE = Pattern.compile(IMAGE_NAME_EXTENSION__REGEX);

    public static void validateOnCreate(File file) {
        log.debug("validateOnCreate... 유효성 검증 -> file : {}", file);

        validateFileName(file.getSavedName());
        validateFileName(file.getOriginalName());
        validateFileSize(file.getFileSize());
    }

    public static void validateOnCreate(List<File> files) {
        log.debug("validateOnCreate... 유효성 검증 -> files : {}", files);
        for (File file : files) {
            validateOnCreate(file);
        }
    }

    private static boolean isValidPositive(Long value) {
        return value != null && value > MIN_NUMBER_VALUE;
    }

    private static boolean isValidSize(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    private static boolean isValidFileSize(Integer size, int min, int max) {
        return size != null && size >= min && size <= max;
    }

    private static void validateFileIdx(Long fileIdx) {
        if (!isValidPositive(fileIdx)) {
            throw new IllegalArgumentException(String.format("파일 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    public static String extractFileExtension(String fileName) {
        Matcher matcher = FileValidationUtils.EXTENSION_PATTERN_COMPILE.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1).toUpperCase();
        }
        return null;
    }

    private static boolean isInvalidImageName(String fileName) {
        String extension = extractFileExtension(fileName);
        return extension == null || !FileNameExtension.contains(extension);
    }


    private static void validateBoardIdx(Long boardIdx) {
        if (!isValidPositive(boardIdx)) {
            throw new IllegalArgumentException(String.format("게시글 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    private static void validateFileName(String fileName) {
        if (isInvalidImageName(fileName)) {
            throw new IllegalArgumentException("유효하지 않은 확장자입니다.");
        }
        if (!isValidSize(fileName, MIN_FILE_NAME_VALUE, MAX_FILE_NAME_VALUE)) {
            throw new IllegalArgumentException(String.format("파일의 이름은 %d글자 이상, %d글자 이하여야만 합니다.", MIN_FILE_NAME_VALUE, MAX_FILE_NAME_VALUE));
        }
    }

    private static void validateFileSize(Integer fileSize) {
        if (!isValidFileSize(fileSize, MIN_FILE_SIZE, MAX_FILE_SIZE)) {
            throw new IllegalArgumentException(String.format("파일의 크기는 %d 이상, %d byte 이하여야만 합니다.", MIN_FILE_SIZE, MAX_FILE_SIZE));
        }
    }

    private static Matcher getMatcher(String imageName) {
        return EXTENSION_PATTERN_COMPILE.matcher(imageName);
    }
}