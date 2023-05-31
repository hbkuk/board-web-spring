package com.study.model.board;

import com.study.ebsoft.model.board.BoardContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("글 내용은")
public class ContentTest {

    @Test
    void create_content() {
        BoardContent content = new BoardContent("내용입니다.");

        assertThat(content).isEqualTo(new BoardContent("내용입니다."));
    }

    @DisplayName("4글자 미만일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"가", "a", "가나", "ab", "가나다", "abc"})
    void invalid_content_shorter_than(String text) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new BoardContent(text);
                })
                .withMessageMatching("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");
    }

    @DisplayName("2000글자 이상인 경우 예외가 발생한다.")
    @Test
    void invalid_content_more_than() {
        StringBuilder longContentSource = new StringBuilder("가나다라");
        while (longContentSource.length() < 2000) {
            longContentSource.append("나");
        }

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new BoardContent(longContentSource.toString());
                })
                .withMessageMatching("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");
    }
}
