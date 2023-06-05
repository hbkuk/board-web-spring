package com.study.ebsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


/**
 * 게시판을 나타내는 클래스입니다.
 *
 * @Builder(toBuilder = true)
 *     : 빌더 패턴을 사용하여 객체를 생성합니다. toBuilder 옵션은 생성된 빌더 객체를 이용해 기존 객체를 복사하고 수정할 수 있도록 합니다.
 * @ToString
 *     : 객체의 문자열 표현을 자동으로 생성합니다. 주요 필드들의 값을 포함한 문자열을 반환합니다.
 * @Getter
 *     : 필드들에 대한 Getter 메서드를 자동으로 생성합니다.
 */
@Builder(toBuilder = true)
@ToString
@Getter
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
     * 게시글의 비밀번호입니다. (JSON 직렬화 시 숨김 처리)
     */
    @JsonIgnore
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

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public Board update(Board updateBoard) {
        if(!isSamePassword(updateBoard.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
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
}

