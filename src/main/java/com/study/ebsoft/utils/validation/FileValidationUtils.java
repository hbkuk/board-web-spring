package com.study.ebsoft.utils.validation;

import com.study.ebsoft.domain.File;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileValidationUtils {

    private static final int MIN_NUMBER_VALUE = 0;

    private static final int MIN_FILE_NAME_VALUE = 5;
    private static final int MAX_FILE_NAME_VALUE = 255;

    private static final int MIN_FILE_SIZE = 1;
    private static final int MAX_FILE_SIZE = 10_485_760;

    private static final String IMAGE_NAME_EXTENSION__REGEX = "\\.(\\w+)$";
    private static final Pattern EXTENSION_PATTERN_COMPILE = Pattern.compile(IMAGE_NAME_EXTENSION__REGEX);

    public static void create(File file) {
        validateFileName(file.getSavedName());
        validateFileName(file.getOriginalName());
        validateFileSize(file.getFileSize());
        validateBoardIdx(file.getBoardIdx());
    }

    public static void find(File file) {
        validateFileIdx(file.getFileIdx());
        validateFileName(file.getSavedName());
        validateFileName(file.getOriginalName());
        validateFileSize(file.getFileSize());
        validateBoardIdx(file.getBoardIdx());
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

    private static boolean isInvalidImageName(String fileName) {
        Matcher matcher = getMatcher(fileName);
        if (!matcher.find()) {
            return true;
        }
        if (!FileNameExtension.contains(matcher.group(1).toUpperCase())) {
            return true;
        }
        return false;
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