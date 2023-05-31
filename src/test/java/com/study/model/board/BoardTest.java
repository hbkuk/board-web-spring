package com.study.model.board;

import com.study.ebsoft.model.board.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Board 도메인의")
public class BoardTest {

    @Nested
    @DisplayName("카테고리는")
    class category {

        @Test
        @DisplayName("필수로 선택되어야 한다.")
        void not_null_category() {
            // given
            Board board = new Board.Builder()
                    .categoryIdx(new CategoryIdx(1))
                    .title(new Title("제목 1"))
                    .writer(new BoardWriter("테스터"))
                    .content(new BoardContent("내용 1"))
                    .password(new Password("rkskekfkakqkt!1"))
                    .build();

            // when
            int category = (int) board.getCategoryIdx().getCategoryIdx();

            //then
            assertThat(category).isNotNull();
        }

        @Test
        @DisplayName("선택되지 않으면 예외가 발생한다.")
        void null_category() {
            // when
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Board.Builder()
                                .title(new Title("제목 1"))
                                .writer(new BoardWriter("테스터"))
                                .content(new BoardContent("내용 1"))
                                .password(new Password("rkskekfkakqkt!1"))
                                .build();
                    }).withMessageMatching("필수값이 입력되지 않았습니다.");
        }
    }

    @Nested
    @DisplayName("제목은")
    class title {

        @Test
        @DisplayName("4글자 이상, 100글자 미만이여야 한다.")
        void title_length_valid() {
            // given
            String shortTitle = "가나다라";
            StringBuilder longTitle = new StringBuilder("가나다라");
            while (longTitle.length() < 99) {
                longTitle.append("나");
            }

            // when
            new Board.Builder()
                    .categoryIdx(new CategoryIdx(1))
                    .title(new Title(shortTitle))
                    .writer(new BoardWriter("테스터"))
                    .content(new BoardContent("내용 1"))
                    .password(new Password("rkskekfkakqkt!1"))
                    .build();

            new Board.Builder()
                    .categoryIdx(new CategoryIdx(1))
                    .title(new Title(longTitle.toString()))
                    .writer(new BoardWriter("테스터"))
                    .content(new BoardContent("내용 1"))
                    .password(new Password("rkskekfkakqkt!1"))
                    .build();
        }

        @Test
        @DisplayName("4글자 미만, 100글자 이상인 경우 예외가 발생한다.")
        void title_length_invalid() {
            // given
            String shortTitle = "가나다";
            StringBuilder longTitleSource = new StringBuilder("가나다라");
            while (longTitleSource.length() < 100) {
                longTitleSource.append("나");
            }
            String longTitle = longTitleSource.toString();

            // when
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                            new Board.Builder()
                                .categoryIdx(new CategoryIdx(1))
                                .title(new Title(shortTitle))
                                .writer(new BoardWriter("테스터"))
                                    .content(new BoardContent("내용 1"))
                                    .password(new Password("rkskekfkakqkt!1"))
                                .build();})
                    .withMessageMatching("제목은 4글자 미만, 99글자 이상을 입력할 수 없습니다.");

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                            new Board.Builder()
                                .categoryIdx(new CategoryIdx(1))
                                .title(new Title(longTitle))
                                .writer(new BoardWriter("테스터"))
                                    .content(new BoardContent("내용 1"))
                                    .password(new Password("rkskekfkakqkt!1"))
                                .build();})
                    .withMessageMatching("제목은 4글자 미만, 99글자 이상을 입력할 수 없습니다.");
        }

    }

    @Nested
    @DisplayName("작성자는")
    class writer {

        @DisplayName("3글자 이상 5글자 미만이여야 한다.")
        @ParameterizedTest
        @ValueSource(strings = {"bob", "jany", "테스터", "내이름은"})
        void writer_length_valid(String writer) {
            // given
                new Board.Builder()
                    .categoryIdx(new CategoryIdx(1))
                    .title(new Title("제목 1"))
                    .writer(new BoardWriter(writer))
                    .content(new BoardContent("내용 1"))
                        .password(new Password("rkskekfkakqkt!1"))
                    .build();
        }

        @DisplayName("3글자 미만 5글자 이상인 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"j", "bo", "가나다라마", "가나다라마바"})
        void writer_length_invalid(String writer) {

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Board.Builder()
                            .categoryIdx(new CategoryIdx(1))
                            .title(new Title("제목 1"))
                            .writer(new BoardWriter(writer))
                                .content(new BoardContent("내용 1"))
                                .password(new Password("rkskekfkakqkt!1"))
                            .build();})
                    .withMessageMatching("작성자를 3글자 미만 5글자 이상을 입력할 수 없습니다.");

        }
    }

    @Nested
    @DisplayName("비밀번호의")
    class password {

        @Nested
        @DisplayName("길이는")
        class length_of {

            @DisplayName("4글자 이상 16글자 미만이여야 한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rk!1", "rkskek!1", "rkskekfkakqkt!1", "!2rkskekfkakqkt"})
            void writer_length_valid(String password) {
                // given
                    new Board.Builder()
                        .categoryIdx(new CategoryIdx(1))
                        .title(new Title("제목 1"))
                        .writer(new BoardWriter("테스터"))
                        .content(new BoardContent("내용 1"))
                        .password(new Password(password))
                        .build();
            }
        }

            @DisplayName("4글자 미만 16글자 이상일 경우 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"r!1", "rkskekfkakqktk1!", "rkskekfkakqktk1!!", "rkskekfkakqktk1@@"})
            void writer_length_valid(String password) {

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                                new Board.Builder()
                                    .categoryIdx(new CategoryIdx(1))
                                    .title(new Title("제목 1"))
                                    .writer(new BoardWriter("테스터"))
                                    .content(new BoardContent("내용 1"))
                                    .password(new Password(password))
                                    .build();
                        })
                        .withMessageMatching("패스워드는 4글자 미만 16글자 이상일 수 없습니다.");
        }

        @Nested
        @DisplayName("필수 조건은")
        class requirement {

            @DisplayName("영문, 숫자, 특수문자가 포함되어 있어야 한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rkskekfkakqkk1!", "rkskekfk@#$!1", "rkskekfk%$#!1", "ndasn11432#@$!@"})
            void password_regex_pass(String password) {
                // given
                    new Board.Builder()
                        .categoryIdx(new CategoryIdx(1))
                        .title(new Title("제목 1"))
                        .writer(new BoardWriter("테스터"))
                        .content(new BoardContent("내용 1"))
                        .password(new Password(password))
                        .build();
            }

            @DisplayName("영문, 숫자, 특수문자가 포함되어 있지 않다면 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rkskekfkakqkk!", "rkskekfk211", "rkskekfk", "11432!#@$@"})
            void password_regex_fail(String password) {
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                                new Board.Builder()
                                    .categoryIdx(new CategoryIdx(1))
                                    .title(new Title("제목 1"))
                                    .writer(new BoardWriter("테스터"))
                                    .content(new BoardContent("내용 1"))
                                    .password(new Password(password))
                                    .build();})
                        .withMessageMatching("패스워드는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.");
            }

        }

        @Nested
        @DisplayName("내용은")
        class content {

            @Test
            @DisplayName("4글자 이상 2000글자 미만이여야 한다.")
            void content_length_valid() {
                // given
                String shortContent = "가나다라";
                StringBuilder longContent = new StringBuilder("가나다라");
                while (longContent.length() < 1999) {
                    longContent.append("나");
                }

                // when
                new Board.Builder()
                        .categoryIdx(new CategoryIdx(1))
                        .title(new Title("제목 1"))
                        .writer(new BoardWriter("테스터"))
                        .content(new BoardContent(shortContent))
                        .password(new Password("rkskekfkakqkt!1"))
                        .build();

                new Board.Builder()
                        .categoryIdx(new CategoryIdx(1))
                        .title(new Title("제목 1"))
                        .writer(new BoardWriter("테스터"))
                        .content(new BoardContent(longContent.toString()))
                        .password(new Password("rkskekfkakqkt!1"))
                        .build();
            }

            @Test
            @DisplayName("4글자 미만 2000글자 이상일 경우 예외가 발생한다.")
            void content_length_invalid() {

                String shortContent = "가나다";
                StringBuilder longContent = new StringBuilder("가나다라");
                while (longContent.length() < 2000) {
                    longContent.append("나");
                }

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            new Board.Builder()
                                    .categoryIdx(new CategoryIdx(1))
                                    .title(new Title("제목 1"))
                                    .writer(new BoardWriter("테스터"))
                                    .content(new BoardContent(shortContent))
                                    .password(new Password("rkskekfkakqkt!1"))
                                    .build();})
                        .withMessageMatching("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            new Board.Builder()
                                    .categoryIdx(new CategoryIdx(1))
                                    .title(new Title("제목 1"))
                                    .writer(new BoardWriter("테스터"))
                                    .content(new BoardContent(longContent.toString()))
                                    .password(new Password("rkskekfkakqkt!1"))
                                    .build();})
                        .withMessageMatching("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");
            }
        }
    }
}
