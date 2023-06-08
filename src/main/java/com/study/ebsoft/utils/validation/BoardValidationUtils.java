package com.study.ebsoft.utils.validation;

import com.study.ebsoft.domain.Board;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class BoardValidationUtils {

    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";

    private static final int MIN_NUMBER_VALUE = 0;

    private static final int MIN_TITLE_VALUE = 4;
    private static final int MAX_TITLE_VALUE = 100;

    private static final int MIN_WRITER_VALUE = 3;
    private static final int MAX_WRITER_VALUE = 4;

    private static final int MIN_CONTENT_VALUE = 4;
    private static final int MAX_CONTENT_VALUE = 2000;

    private static final int MIN_PASSWORD_VALUE = 4;
    private static final int MAX_PASSWORD_VALUE = 15;


    public static void validateOnCreate(Board board) {
        log.debug("validateOnCreate... 유효성 검증 -> board : {} ", board.toString());

        validateCategoryIdx(board.getCategoryIdx());
        validateTitle(board.getTitle());
        validateWriter(board.getWriter());
        validateContent(board.getContent());
        validatePassword(board.getPassword());
    }

    public static void validateOnUpdate(Board board) {
        log.debug("validateOnUpdate.... 유효성 검증 -> board : {} ", board.toString());

        validateCategoryIdx(board.getCategoryIdx());
        validateTitle(board.getTitle());
        validateWriter(board.getWriter());
        validateContent(board.getContent());
        validatePassword(board.getPassword());
    }

    private static boolean isEmpty(String value) {
        return value != null && value.equals("");
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

    private static void validateBoardIdx(Long boardIdx) {
        if (!isValidPositive(boardIdx)) {
            throw new IllegalArgumentException(String.format("게시글 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    private static void validateCategoryIdx(Integer categoryIdx) {
        if (!isValidPositive(categoryIdx)) {
            throw new IllegalArgumentException(String.format("카테고리 번호는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    private static void validateTitle(String title) {
        if (!isValidSize(title, MIN_TITLE_VALUE, MAX_TITLE_VALUE)) {
            throw new IllegalArgumentException(String.format("제목은 %d글자 이상, %d글자 이하로 입력해야 합니다.", MIN_TITLE_VALUE, MAX_TITLE_VALUE));
        }
    }

    private static void validateWriter(String writer) {
        if (!isValidSize(writer, MIN_WRITER_VALUE, MAX_WRITER_VALUE)) {
            throw new IllegalArgumentException(String.format("작성자는 %d글자 이상, %d글자 이하로 입력해야 합니다.", MIN_WRITER_VALUE, MAX_WRITER_VALUE));
        }
    }

    private static void validateContent(String content) {
        if (!isValidSize(content, MIN_CONTENT_VALUE, MAX_CONTENT_VALUE)) {
            throw new IllegalArgumentException(String.format("내용은 %d글자 이상, %d글자 이하로 입력해야 합니다.", MIN_CONTENT_VALUE, MAX_CONTENT_VALUE));
        }
    }

    private static void validatePassword(String password) {
        if (!isValidSize(password, MIN_PASSWORD_VALUE, MAX_PASSWORD_VALUE)) {
            throw new IllegalArgumentException(String.format("패스워드는 %d글자 이상, %d글자 이하로 입력해야 합니다.", MIN_PASSWORD_VALUE, MAX_PASSWORD_VALUE));
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("패스워드는 영문, 숫자, 특수문자를 포함해야 합니다.");
        }
    }

    private static void validateHit(Integer hit) {
        if (!isValidPositive(hit)) {
            throw new IllegalArgumentException(String.format("조회수는 %d보다 큰 숫자여야 합니다.", MIN_NUMBER_VALUE));
        }
    }

    private static void validateRegDate(LocalDateTime regDate) {
        if (regDate == null) {
            throw new IllegalArgumentException("등록일시는 필수입니다.");
        }
    }

    private static void validateModDate(LocalDateTime modDate) {
        if (modDate == null) {
            throw new IllegalArgumentException("수정일시는 필수입니다.");
        }
    }
}
