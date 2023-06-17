package com.study.ebsoft.exception;

/**
 * 게시물 번호에 해당한 게시물이 없을때 던져지는 Unchecked Exception 입니다.
 */
public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException(String message) {
        super(message);
    }
}
