package com.study.model.board;

import com.study.ebsoft.model.board.BoardWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("작성자는")
public class WriterTest {

    @Test
    void create() {
        // given
        String name = "테스터";
        BoardWriter writer = new BoardWriter(name);

        // when
        boolean acture = writer.equals(new BoardWriter(name));

        // then
        assertThat(acture).isTrue();
    }

    @DisplayName("3글자 미만일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"j", "가", "1", "bo", "나다", "12"})
    void invalid_writer_shorter_than(String name) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    BoardWriter writer = new BoardWriter(name);
                })
                .withMessageMatching("작성자를 3글자 미만 5글자 이상을 입력할 수 없습니다.");
    }

    @DisplayName("5글자 이상일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"가나다라마", "abcde", "가나다라마바", "abcdef"})
    void invalid_writer_longer_than(String name) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    BoardWriter writer = new BoardWriter(name);
                })
                .withMessageMatching("작성자를 3글자 미만 5글자 이상을 입력할 수 없습니다.");
    }
}
