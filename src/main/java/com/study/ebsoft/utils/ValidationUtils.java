package com.study.ebsoft.utils;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.domain.File;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ValidationUtils {

    // 비밀번호 정규식
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";

    // 최소 숫자 값
    private static final int MIN_NUMBER_VALUE = 0;

    // 게시글 제목 최소/최대 글자 수
    private static final int MIN_BOARD_TITLE_VALUE = 4;
    private static final int MAX_BOARD_TITLE_VALUE = 100;

    // 게시글 작성자 최소/최대 글자 수
    private static final int MIN_BOARD_WRITER_VALUE = 3;
    private static final int MAX_BOARD_WRITER_VALUE = 4;

    // 게시글 내용 최소/최대 글자 수
    private static final int MIN_BOARD_CONTENT_VALUE = 4;
    private static final int MAX_BOARD_CONTENT_VALUE = 2000;

    // 게시글 비밀번호 최소/최대 글자 수
    private static final int MIN_BOARD_PASSWORD_VALUE = 4;
    private static final int MAX_BOARD_PASSWORD_VALUE = 15;

    // 댓글 내용 최소/최대 글자 수
    private static final int MIN_COMMENT_CONTENT_VALUE = 4;
    private static final int MAX_COMMENT_CONTENT_VALUE = 1000;

    // 댓글 작성자 최소/최대 글자 수
    private static final int MIN_COMMENT_WRITER_VALUE = 3;
    private static final int MAX_COMMENT_WRITER_VALUE = 4;

    // 댓글 비밀번호 최소/최대 글자 수
    private static final int MIN_COMMENT_PASSWORD_VALUE = 4;
    private static final int MAX_COMMENT_PASSWORD_VALUE = 15;

    // 파일 크기 제한
    private static final int MIN_FILE_SIZE = 1;
    private static final int MAX_FILE_SIZE = 10_485_760;

    // 파일 이름 최소/최대 글자 수
    private static final int MIN_FILE_NAME_VALUE = 5;
    private static final int MAX_FILE_NAME_VALUE = 255;

    /**
     * 게시글의 유효성을 검사합니다.
     *
     * @param board 게시글 객체
     */
    public static void validateBoard(Board board) {
        log.debug("validateBoardOnCreate... 유효성 검증 -> board : {} ", board);

        validateCategoryIdx(board.getCategoryIdx());
        validateTitle(board.getTitle(), MIN_BOARD_TITLE_VALUE, MAX_BOARD_TITLE_VALUE);
        validateWriter(board.getWriter(), MIN_BOARD_WRITER_VALUE, MAX_BOARD_WRITER_VALUE);
        validateContent(board.getContent(), MIN_BOARD_CONTENT_VALUE, MAX_BOARD_CONTENT_VALUE);
        validatePassword(board.getPassword(), MIN_BOARD_PASSWORD_VALUE, MAX_BOARD_PASSWORD_VALUE);
    }

    /**
     * 댓글의 유효성을 검사합니다.
     *
     * @param comment 댓글 객체
     */
    public static void validateComment(Comment comment) {
        log.debug("validateCommentOnCreate.... 유효성 검증 -> board : {} ", comment);

        validateWriter(comment.getWriter(), MIN_COMMENT_WRITER_VALUE, MAX_COMMENT_WRITER_VALUE);
        validateContent(comment.getContent(), MIN_COMMENT_CONTENT_VALUE, MAX_COMMENT_CONTENT_VALUE);
        validatePassword(comment.getPassword(), MIN_COMMENT_PASSWORD_VALUE, MAX_COMMENT_PASSWORD_VALUE);
        validateBoardIdx(comment.getBoardIdx());
    }

    /**
     * 파일의 유효성을 검사합니다.
     *
     * @param file 파일 객체
     */
    public static void validateFileOnCreate(File file) {
        log.debug("validateOnCreate... 유효성 검증 -> file : {}", file);

        validateFileName(file.getSavedName());
        validateFileName(file.getOriginalName());
        validateFileSize(file.getFileSize());
    }

    /**
     * 파일 리스트의 유효성을 검사합니다.
     *
     * @param files 파일 객체 리스트
     */
    public static void validateFileOnCreate(List<File> files) {
        log.debug("validateOnCreate... 유효성 검증 -> files : {}", files);
        for (File file : files) {
            validateFileOnCreate(file);
        }
    }

    /**
     * 카테고리 번호의 유효성을 검사합니다.
     *
     * @param categoryIdx 카테고리 번호
     * @throws IllegalArgumentException 카테고리 번호가 유효하지 않을 경우 예외 발생
     */
    private static void validateCategoryIdx(Integer categoryIdx) {
        if (!isValidPositive(categoryIdx)) {
            throw new IllegalArgumentException(String.format("카테고리 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    /**
     * 제목의 길이 유효성을 검사합니다.
     *
     * @param title 제목
     * @param min 최소 길이
     * @param max 최대 길이
     * @throws IllegalArgumentException 제목의 길이가 유효하지 않을 경우 예외 발생
     */
    private static void validateTitle(String title, int min, int max) {
        if (!isValidSize(title, min, max)) {
            throw new IllegalArgumentException(String.format("제목은 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
    }

    /**
     * 작성자의 길이 유효성을 검사합니다.
     *
     * @param writer 작성자
     * @param min 최소 길이
     * @param max 최대 길이
     * @throws IllegalArgumentException 작성자의 길이가 유효하지 않을 경우 예외 발생
     */
    private static void validateWriter(String writer, int min, int max) {
        if (!isValidSize(writer, min, max)) {
            throw new IllegalArgumentException(String.format("작성자는 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
    }

    /**
     * 내용의 길이 유효성을 검사합니다.
     *
     * @param content 내용
     * @param min 최소 길이
     * @param max 최대 길이
     * @throws IllegalArgumentException 내용의 길이가 유효하지 않을 경우 예외 발생
     */
    private static void validateContent(String content, int min, int max) {
        if (!isValidSize(content, min, max)) {
            throw new IllegalArgumentException(String.format("내용은 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
    }

    /**
     * 패스워드의 길이와 조건 유효성을 검사합니다.
     *
     * @param password 패스워드
     * @param min 최소 길이
     * @param max 최대 길이
     * @throws IllegalArgumentException 패스워드의 길이나 조건이 유효하지 않을 경우 예외 발생
     */
    private static void validatePassword(String password, int min, int max) {
        if (!isValidSize(password, min, max)) {
            throw new IllegalArgumentException(String.format("패스워드는 %d글자 이상, %d글자 이하로 입력해야 합니다.", min, max));
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("패스워드는 영문, 숫자, 특수문자를 포함해야 합니다.");
        }
    }

    /**
     * 게시글 번호의 유효성을 검사합니다.
     *
     * @param boardIdx 게시글 번호
     * @throws IllegalArgumentException 게시글 번호가 유효하지 않을 경우 예외 발생
     */
    private static void validateBoardIdx(Long boardIdx) {
        if (!isValidPositive(boardIdx)) {
            throw new IllegalArgumentException(String.format("게시글 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    /**
     * 댓글 번호의 유효성을 검사합니다.
     *
     * @param commentIdx 댓글 번호
     * @throws IllegalArgumentException 댓글 번호가 유효하지 않을 경우 예외 발생
     */
    private static void validateCommentIdx(Long commentIdx) {
        if (!isValidPositive(commentIdx)) {
            throw new IllegalArgumentException(String.format("댓글 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    /**
     * 파일 이름의 유효성을 검사합니다.
     *
     * @param fileName 파일 이름
     * @throws IllegalArgumentException 파일 이름이 유효하지 않을 경우 예외 발생
     */
    private static void validateFileName(String fileName) {
        if (!isValidFileName(fileName)) {
            throw new IllegalArgumentException("유효하지 않은 확장자입니다.");
        }
        if (!isValidSize(fileName, MIN_FILE_NAME_VALUE, MAX_FILE_NAME_VALUE)) {
            throw new IllegalArgumentException(String.format("파일의 이름은 %d글자 이상, %d글자 이하여야만 합니다.", MIN_FILE_NAME_VALUE, MAX_FILE_NAME_VALUE));
        }
    }

    /**
     * 파일 크기의 유효성을 검사합니다.
     *
     * @param fileSize 파일 크기
     * @throws IllegalArgumentException 파일 크기가 유효하지 않을 경우 예외 발생
     */
    private static void validateFileSize(Integer fileSize) {
        if (!isValidFileSize(fileSize, MIN_FILE_SIZE, MAX_FILE_SIZE)) {
            throw new IllegalArgumentException(String.format("파일의 크기는 %d 이상, %d byte 이하여야만 합니다.", MIN_FILE_SIZE, MAX_FILE_SIZE));
        }
    }

    /**
     * 값이 양수인지 확인합니다.
     *
     * @param value 값
     * @return 양수 여부
     */
    private static boolean isValidPositive(Long value) {
        return value != null && value > MIN_NUMBER_VALUE;
    }

    /**
     * 값이 양수인지 확인합니다.
     *
     * @param value 값
     * @return 양수 여부
     */
    private static boolean isValidPositive(Integer value) {
        return value != null && value > MIN_NUMBER_VALUE;
    }

    /**
     * 문자열의 길이가 주어진 범위 내에 있는지 확인합니다.
     *
     * @param value 문자열
     * @param min 최소 길이
     * @param max 최대 길이
     * @return 길이 유효성 여부
     */
    private static boolean isValidSize(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    /**
     * 패스워드의 유효성을 확인합니다.
     *
     * @param value 패스워드
     * @return 패스워드 유효성 여부
     */
    private static boolean isValidPassword(String value) {
        return value != null && value.matches(PASSWORD_REGEX);
    }

    /**
     * 문자열이 비어있는지 확인합니다.
     *
     * @param value 문자열
     * @return 비어있는지 여부
     */
    private static boolean isEmpty(String value) {
        return value != null && !value.equals("");
    }

    /**
     * 파일 크기가 주어진 범위 내에 있는지 확인합니다.
     *
     * @param size 파일 크기
     * @param min 최소 크기
     * @param max 최대 크기
     * @return 파일 크기 유효성 여부
     */
    private static boolean isValidFileSize(Integer size, int min, int max) {
        return size != null && size >= min && size <= max;
    }

    /**
     * 파일 이름의 유효성을 확인합니다.
     *
     * @param fileName 파일 이름
     * @return 파일 이름 유효성 여부
     */
    private static boolean isValidFileName(String fileName) {
        String extension = FileUtils.extractFileExtension(fileName);
        return extension != null && FileNameExtension.contains(extension);
    }


}
