package com.study.ebsoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * 파일을 나타내는 클래스입니다.
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
public class File {

    /**
     * 파일의 고유 식별자입니다.
     */
    @Builder.Default
    private Long fileIdx = 0L;

    /**
     * 디렉토리에 저장된 파일이름입니다. (JSON 직렬화 시 숨김 처리)
     */
    @JsonIgnore
    private String savedName;

    /**
     * 사용자가 알고있는, 실제 파일 이름입니다.
     */
    private String originalName;

    /**
     * 파일의 크기입니다.
     */
    private Integer fileSize;

    /**
     * 게시글의 고유 식별자입니다.
     */
    private Long boardIdx;
}
