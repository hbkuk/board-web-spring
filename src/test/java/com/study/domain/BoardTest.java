package com.study.domain;

import com.study.ebsoft.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    @Test
    @DisplayName("게시글의 패스워드가 같다면 true를 리턴한다")
    void equal_password() {
        // given
        String password = "rkskekfkakqkt!1";
        Board board = Board.builder()
                .categoryIdx(1)
                .title("제목 1")
                .writer("테스터")
                .content("내용 1")
                .password(password)
                .build();

        // when
        boolean acture = board.isSamePassword(password);

        // then
        assertThat(acture).isEqualTo(true);
    }

    @ParameterizedTest
    @CsvSource(value = {"rkskekfkakqkt!1:rkskekfkakqkt@1", "rkskekf!!!1:rkskekfkakqkt!!@1"}, delimiter = ':')
    @DisplayName("게시글의 패스워드가 다르다면 false 리턴한다")
    void not_equal_password(String passwordA, String passwordB) {
        // given
        Board board = Board.builder()
                .categoryIdx(1)
                .title("제목 1")
                .writer("테스터")
                .content("내용 1")
                .password(passwordA)
                .build();

        // when
        boolean acture = board.isSamePassword(passwordB);

        // then
        assertThat(acture).isEqualTo(false);
    }

    @Test
    @DisplayName("update 메서드는 게시글을 수정한다.")
    void update() {
        // given
        Board board = Board.builder()
                .boardIdx(1L)
                .categoryIdx(1)
                .title("제목 1")
                .writer("테스터")
                .content("내용 1")
                .password("rkskekfkakqkt!1")
                .hit(1)
                .regDate(LocalDateTime.now())
                .build();

        Board updateBoard = Board.builder()
                .boardIdx(1L)
                .categoryIdx(3)
                .title("제목수정 1")
                .writer("테스터1")
                .content("내용수정 1")
                .password("rkskekfkakqkt!1")
                .build();

        Board updatedBoard = board.update(updateBoard);

        // then
        assertThat(updatedBoard.getBoardIdx()).isEqualTo(1L);
        assertThat(updatedBoard.getCategoryIdx()).isEqualTo(3);
        assertThat(updatedBoard.getTitle()).isEqualTo("제목수정 1");
        assertThat(updatedBoard.getWriter()).isEqualTo("테스터1");
        assertThat(updatedBoard.getContent()).isEqualTo("내용수정 1");
        assertThat(LocalDateTime.now()).isAfter(updatedBoard.getModDate());
    }

    @Test
    @DisplayName("canDelete 메서드는 비밀번호가 같다면 true를 리턴한다.")
    void can_delete() {

        Board board = Board.builder()
                .boardIdx(1L)
                .categoryIdx(1)
                .title("제목 1")
                .writer("테스터")
                .content("내용 1")
                .password("rkskekfkakqkt!1")
                .hit(1)
                .regDate(LocalDateTime.now())
                .build();

        String passwordToDelete = "rkskekfk@@1";

        assertThat(board.canDelete(passwordToDelete)).isEqualTo(false);
    }

}
