package com.study.ebsoft.dto;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.File;
import lombok.*;

import java.util.List;

/**
 * 검색조건 결과를 전달하는 클래스입니다.
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
