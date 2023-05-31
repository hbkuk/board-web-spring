package com.study.model.comment;

import com.study.ebsoft.model.comment.CommentIdx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("CommentIdx 클래스는")
public class CommentIdxTest {

    @DisplayName("생성자의 매개변수는 정수만 허용된다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 100, 2000, 3000, 40000, 50000})
    void create_commentIdx(int number) {
        CommentIdx commentIdx = new CommentIdx(number);

        assertThat(commentIdx).isEqualTo(new CommentIdx(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3, -1000, -20000})
    @DisplayName("음수가 전달될 경우 예외가 발생한다.")
    void invalid_commentIdx(int number) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new CommentIdx(number);
                })
                .withMessageMatching("글 번호는 음수일 수 없습니다.");
    }
}
