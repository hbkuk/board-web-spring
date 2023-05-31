package com.study.model.board;

import com.study.ebsoft.model.board.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThat;

public class HitTest {

    @Test
    void create_hit() {
        Hit hit = new Hit(10);

        assertThat(hit).isEqualTo(new Hit(10));
    }

    @DisplayName("조회수가 음수일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3, Integer.MIN_VALUE})
    void invalid_hit(int hit) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Hit(hit);
                })
                .withMessageMatching("조회수는 음수일 수 없습니다.");
    }

    @DisplayName("조회수가 증가한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:2", "3:4", "5:6", "100:101", "1000:1001"}, delimiter = ':')
    void increase_hit(int beforeIncreaseHit, int afterIncreaseHit) {
        // given
        Hit hit = new Hit(beforeIncreaseHit);

        // when
        hit = hit.increase();

        // then
        assertThat(hit.getHit()).isEqualTo(afterIncreaseHit);
    }

    @Test
    void create_new_board() {
        // given
        Board board = new Board.Builder()
                .categoryIdx(new CategoryIdx(1))
                .title(new Title("제목 1"))
                .writer(new BoardWriter("테스터"))
                .content(new BoardContent("내용 1"))
                .password(new Password("rkskekfkakqkt!1"))
                .build();

        // when
        assertThat(board.getHit().getHit()).isNotNull();
    }
}
