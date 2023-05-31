package com.study.model.comment;

import com.study.ebsoft.model.comment.CommentContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CommentContentTest {

    @Test
    void create_comment() {
        CommentContent content = new CommentContent("테스트 내용");

        assertThat(content).isEqualTo(new CommentContent("테스트 내용"));
    }

    @Test
    @DisplayName("4글자 미만 1000글자 이상일 경우 예외가 발생한다.")
    void content_length_invalid() {

        String shortContent = "가나다";
        StringBuilder longContent = new StringBuilder("가나다라");
        while (longContent.length() < 1000) {
            longContent.append("나");
        }

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new CommentContent(shortContent);
                })
                .withMessageMatching("내용은 4글자 미만 1000글자를 초과할 수 없습니다.");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new CommentContent(shortContent);
                })
                .withMessageMatching("내용은 4글자 미만 1000글자를 초과할 수 없습니다.");
    }
}
