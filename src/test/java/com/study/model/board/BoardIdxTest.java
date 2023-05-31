package com.study.model.board;

import com.study.ebsoft.model.board.BoardIdx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("BoardId 클래스는")
public class BoardIdxTest {

    @DisplayName("생성자의 매개변수는 정수만 허용된다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 100, 2000, 3000, 40000, 50000})
    void create_boardId(int number) {
        BoardIdx boardId = new BoardIdx(number);

        assertThat(boardId).isEqualTo(new BoardIdx(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3, -1000, -20000})
    @DisplayName("음수가 전달될 경우 예외가 발생한다.")
    void invalid_boardId(int number) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new BoardIdx(number);
                })
                .withMessageMatching("글 번호는 음수일 수 없습니다.");
    }
}
