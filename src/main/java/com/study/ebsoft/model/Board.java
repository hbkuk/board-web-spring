package com.study.ebsoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


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
    @Builder.Default
    @Positive(message = "게시글 번호는 양수이어야 합니다.")
    private final Long boardIdx = 0L;

    /**
     * 카테고리의 식별자입니다.
     */
    @Positive(message = "카테고리 번호는 양수이어야 합니다.")
    private final Integer categoryIdx;

    /**
     * 게시글의 제목입니다.
     */
    @Size(min = 4, max = 100, message = "제목은 4글자 이상, 100글자 이하로 입력해야 합니다.")
    private final String title;

    /**
     * 게시글 작성자입니다.
     */
    @Size(min = 3, max = 4, message = "작성자는 3글자 이상, 4글자 이하로 입력해야 합니다.")
    private final String writer;

    /**
     * 게시글의 내용입니다.
     */
    @Size(min = 4, max = 2000, message = "내용은 4글자 이상, 2000글자 이하로 입력해야 합니다.")
    private final String content;

    /**
     * 게시글의 비밀번호입니다. (JSON 직렬화 시 숨김 처리)
     */
    @JsonIgnore
    @Size(min = 4, max = 15, message = "패스워드는 4글자 이상, 15글자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$", message = "패스워드는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private final String password;

    /**
     * 조회수입니다.
     */
    @Builder.Default
    @Positive(message = "조회수는 양수이어야 합니다.")
    private final Integer hit = 0;

    /**
     * 등록일시입니다.
     */
    @Builder.Default
    private final LocalDateTime regDate = LocalDateTime.now();

    /**
     * 수정일시입니다.
     */
    @Builder.Default
    private final LocalDateTime modDate = null;

}

