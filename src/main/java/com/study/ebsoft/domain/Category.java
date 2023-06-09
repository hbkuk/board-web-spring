package com.study.ebsoft.domain;

import lombok.*;

/**
 * 카테고리를 나타내는 클래스입니다.
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
public class Category {

    /**
     * 카테고리의 고유 식별자입니다.
     */
    private final Long categoryIdx;

    /**
     * 카테고리의 코드입니다.
     */
    private final String code;

    /**
     * 카테고리의 이름입니다.
     */
    private final String name;

}
