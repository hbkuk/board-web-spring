package com.study.model.board;

import com.study.ebsoft.model.board.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Title 클래스는")
public class TitleTest {

    @Test
    void create_title() {
        //given
        String text = "제목 1";

        // when
        Title title = new Title(text);

        // then
        assertThat(title).isEqualTo(new Title(text));
    }

    @ParameterizedTest
    @ValueSource(strings = {"가", "가나", "가나다"})
    @DisplayName("제목이 4글자 미만인 경우 예외가 발생한다.")
    void invalid_title_shorter_than(String text) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Title(text);
                })
                .withMessageMatching("제목은 4글자 미만, 99글자 이상을 입력할 수 없습니다.");
    }

    @Test
    @DisplayName("제목이 99글자 이상인 경우 예외가 발생한다.")
    void invalid_title_more_than() {
        // given
        StringBuilder longTitleSource = new StringBuilder("가나다라");
        while (longTitleSource.length() < 100) {
            longTitleSource.append("나");
        }
        String longTitle = longTitleSource.toString();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Title(longTitle);
                })
                .withMessageMatching("제목은 4글자 미만, 99글자 이상을 입력할 수 없습니다.");
    }

    @Test
    @DisplayName("문자열의 길이가 80 이상인 경우 true를 반환한다.")
    void more_than_80_length() {
        // given
        StringBuilder text = new StringBuilder("가나다라");
        while (text.length() < 80) {
            text.append("가");
        }

        // when
        Title title = new Title(text.toString());
        boolean acture = title.isLongTitle();

        // then
        assertThat(acture).isTrue();
    }

    @Test
    @DisplayName("문자열의 길이가 80 미만일 경우 false를 반환한다.")
    void shorter_than_80_length() {
        // given
        StringBuilder text = new StringBuilder("가나다라");
        while (text.length() < 79) {
            text.append("가");
        }

        // when
        Title title = new Title(text.toString());
        boolean acture = title.isLongTitle();

        // then
        assertThat(acture).isFalse();
    }
}
