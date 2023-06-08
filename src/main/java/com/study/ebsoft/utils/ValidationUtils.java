package com.study.ebsoft.utils;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.domain.File;
import com.study.ebsoft.utils.validation.FileNameExtension;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ValidationUtils {

    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";

    private static final String FILE_NAME_EXTENSION_REGEX = "\\.(\\w+)$";
    private static final Pattern EXTENSION_PATTERN_COMPILE = Pattern.compile(FILE_NAME_EXTENSION_REGEX);

    private static final int MIN_NUMBER_VALUE = 0;

    private static final int MIN_BOARD_TITLE_VALUE = 4;
    private static final int MAX_BOARD_TITLE_VALUE = 100;
    private static final int MIN_BOARD_WRITER_VALUE = 3;
    private static final int MAX_BOARD_WRITER_VALUE = 4;
    private static final int MIN_BOARD_CONTENT_VALUE = 4;
    private static final int MAX_BOARD_CONTENT_VALUE = 2000;
    private static final int MIN_BOARD_PASSWORD_VALUE = 4;
    private static final int MAX_BOARD_PASSWORD_VALUE = 15;

    private static final int MIN_COMMENT_CONTENT_VALUE= 4;
    private static final int MAX_COMMENT_CONTENT_VALUE = 1000;
    private static final int MIN_COMMENT_WRITER_VALUE = 3;
    private static final int MAX_COMMENT_WRITER_VALUE = 4;
    private static final int MIN_COMMENT_PASSWORD_VALUE = 4;
    private static final int MAX_COMMENT_PASSWORD_VALUE = 15;

    private static final int MIN_FILE_SIZE = 1;
    private static final int MAX_FILE_SIZE = 10_485_760;
    private static final int MIN_FILE_NAME_VALUE = 5;
    private static final int MAX_FILE_NAME_VALUE = 255;



    public static void validateBoard(Board board) {
        log.debug("validateBoardOnCreate... 유효성 검증 -> board : {} ", board);

        validateCategoryIdx(board.getCategoryIdx());
        validateTitle(board.getTitle(), MIN_BOARD_TITLE_VALUE, MAX_BOARD_TITLE_VALUE);
        validateWriter(board.getWriter(), MIN_BOARD_WRITER_VALUE, MAX_BOARD_WRITER_VALUE);
        validateContent(board.getContent(), MIN_BOARD_CONTENT_VALUE, MAX_BOARD_CONTENT_VALUE);
        validatePassword(board.getPassword(), MIN_BOARD_PASSWORD_VALUE, MAX_BOARD_PASSWORD_VALUE);
    }

    public static void validateComment(Comment comment) {
        log.debug("validateCommentOnCreate.... 유효성 검증 -> board : {} ", comment);

        validateWriter(comment.getWriter(), MIN_COMMENT_WRITER_VALUE, MAX_COMMENT_WRITER_VALUE);
        validateContent(comment.getContent(), MIN_COMMENT_CONTENT_VALUE, MAX_COMMENT_CONTENT_VALUE);
        validatePassword(comment.getPassword(), MIN_COMMENT_PASSWORD_VALUE, MAX_COMMENT_PASSWORD_VALUE);
        validateBoardIdx(comment.getBoardIdx());
    }

    public static void validateFileOnCreate(File file) {
        log.debug("validateOnCreate... 유효성 검증 -> file : {}", file);

        validateFileName(file.getSavedName());
        validateFileName(file.getOriginalName());
        validateFileSize(file.getFileSize());
    }

    public static void validateFileOnCreate(List<File> files) {
        log.debug("validateOnCreate... 유효성 검증 -> files : {}", files);
        for (File file : files) {
            validateFileOnCreate(file);
        }
    }

    private static void validateCategoryIdx(Integer categoryIdx) {
        if (!isValidPositive(categoryIdx)) {
            throw new IllegalArgumentException(String.format("카테고리 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    private static void validateTitle(String title, int min, int max) {
        if (!isValidSize(title, min, max)) {
            throw new IllegalArgumentException(String.format("제목은 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
    }

    private static void validateWriter(String writer, int min, int max) {
        if (!isValidSize(writer, min, max)) {
            throw new IllegalArgumentException(String.format("작성자는 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
    }

    private static void validateContent(String content, int min, int max) {
        if (!isValidSize(content, min, max)) {
            throw new IllegalArgumentException(String.format("내용은 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
    }

    private static void validatePassword(String password, int min, int max) {
        if (!isValidSize(password, min, max)) {
            throw new IllegalArgumentException(String.format("패스워드는 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("패스워드는 영문, 숫자, 특수문자를 포함해야 합니다.");
        }
    }

    private static void validateBoardIdx(Long boardIdx) {
        if (!isValidPositive(boardIdx)) {
            throw new IllegalArgumentException(String.format("게시글 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    private static void validateCommentIdx(Long commentIdx) {
        if (!isValidPositive(commentIdx)) {
            throw new IllegalArgumentException(String.format("댓글 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    private static void validateFileName(String fileName) {
        if (!isValidFileName(fileName)) {
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

    private static boolean isValidPositive(Long value) {
        return value != null && value > MIN_NUMBER_VALUE;
    }

    private static boolean isValidPositive(Integer value) {
        return value != null && value > MIN_NUMBER_VALUE;
    }

    private static boolean isValidSize(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    private static boolean isValidPassword(String value) {
        return value != null && value.matches(PASSWORD_REGEX);
    }

    private static boolean isEmpty(String value) {
        return value != null && !value.equals("");
    }

    private static boolean isValidFileSize(Integer size, int min, int max) {
        return size != null && size >= min && size <= max;
    }

    private static boolean isValidFileName(String fileName) {
        String extension = extractFileExtension(fileName);
        return extension != null && FileNameExtension.contains(extension);
    }

    public static String extractFileExtension(String fileName) {
        Matcher matcher = EXTENSION_PATTERN_COMPILE.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1).toUpperCase();
        }
        return null;
    }
}
