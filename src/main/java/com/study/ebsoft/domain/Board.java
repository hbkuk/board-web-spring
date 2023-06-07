package com.study.ebsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;


/**
 * 게시글을 나타내는 클래스입니다.
 *
 * @Builder(toBuilder = true)
 *     : 빌더 패턴을 사용하여 객체를 생성합니다. toBuilder 옵션은 생성된 빌더 객체를 이용해 기존 객체를 복사하고 수정할 수 있도록 합니다.
 * @ToString
 *     : 객체의 문자열 표현을 자동으로 생성합니다. 주요 필드들의 값을 포함한 문자열을 반환합니다.
 * @Getter
 *     : 필드들에 대한 Getter 메서드를 자동으로 생성합니다.
 * @NoArgsConstructor(force = true)
 *     : 매개변수가 없는 기본 생성자를 자동으로 생성합니다. MyBatis 또는 JPA 라이브러리에서는 기본 생성자를 필요로 합니다.
 * @AllArgsConstructor
 *     : 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다.
 */
@Builder(toBuilder = true)
@ToString
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Board {

    /**
     * 게시글의 고유 식별자입니다.
     */
    private final Long boardIdx;

    /**
     * 카테고리의 식별자입니다.
     */
    private final Integer categoryIdx;

    /**
     * 카테고리 내용입니다.
     */
    private final String categoryName;

    /**
     * 게시글의 제목입니다.
     */
    private final String title;

    /**
     * 게시글 작성자입니다.
     */
    private final String writer;

    /**
     * 게시글의 내용입니다.
     */
    private final String content;

    /**
     * 게시글의 비밀번호입니다.
     */
    private final String password;

    /**
     * 조회수입니다.
     */
    private final Integer hit;

    /**
     * 등록일시입니다.
     */
    private final LocalDateTime regDate;

    /**
     * 수정일시입니다.
     */
    private final LocalDateTime modDate;

    /**
     * 입력받은 비밀번호와 현재 객체의 비밀번호가 일치하다면 true, 그렇지 않으면 false를 리턴합니다
     *
     * @param password 비교할 비밀번호
     * @return 비밀번호가 일치하면 true, 그렇지 않으면 false를 반환합니다.
     */
    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    /**
     * 업데이트 된 게시글을 리턴합니다
     *
     * @param updateBoard 업데이트할 게시글 객체
     * @return 업데이트된 게시글 객체를 반환합니다.
     */
    public Board update(Board updateBoard) {
        return Board.builder()
                .boardIdx(this.boardIdx)
                .categoryIdx(updateBoard.getCategoryIdx())
                .title(updateBoard.getTitle())
                .writer(updateBoard.getWriter())
                .content(updateBoard.getContent())
                .password(this.password)
                .hit(this.hit)
                .regDate(this.regDate)
                .modDate(LocalDateTime.now())
                .build();
    }

    /**
     * 인자로 받은 패스워드로 게시글을 삭제할 수 있다면 true, 그렇지 않다면 false를 리턴합니다.
     *
     * @param deleteBoard 삭제할 객체
     * @return 게시글을 삭제할 수 있다면 true, 그렇지 않다면 false를 리턴합니다.
     */
    public boolean canDelete(Board deleteBoard) {
        return deleteBoard.isSamePassword(this.password);
    }

    /**
     * 인자로 받은 패스워드로 게시글을 수정할 수 있다면 true, 그렇지 않다면 false를 리턴합니다.
     *
     * @param password 비밀번호
     * @return 게시글을 수정할 수 있다면 true, 그렇지 않다면 false를 리턴합니다.
     */
    public boolean canUpdate(String password) {
        return isSamePassword(password);
    }
}

