package com.study.ebsoft.dto;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.File;
import lombok.*;

/**
 * 검색조건을 나타내는 클래스입니다.
 */
@Data
@Builder(toBuilder = true)
@ToString
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class BoardDTO {
    private Board board;
    private File file;
}
