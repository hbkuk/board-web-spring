package com.study.utils;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.utils.ValidationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@DisplayName("Board 도메인의")
public class BoardValidationTest {

    @Nested
    @DisplayName("카테고리는")
    class category {

        @Test
        @DisplayName("필수로 선택되어야 한다.")
        void not_null_category() {
            // given
            Board board = Board.builder()
                    .categoryIdx(1)
                    .title("제목 1")
                    .writer("테스터")
                    .content("내용 1")
                    .password("rkskekfkakqkt!1")
                    .build();

            // when
            ValidationUtils.validateBoard(board);

            // then
            assertThat(board.getCategoryIdx()).isNotNull();
            assertThat(board.getCategoryIdx()).isEqualTo(1);
        }

        @Test
        @DisplayName("선택되지 않으면 예외가 발생한다.")
        void null_category() {
            // when
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Board board = Board.builder()
                                .title("제목 1")
                                .writer("테스터")
                                .content("내용 1")
                                .password("rkskekfkakqkt!1")
                                .build();
                        ValidationUtils.validateBoard(board);
                    }).withMessageMatching("카테고리 번호는 0보다 큰 숫자여야 합니다.");
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

            Board boardA = Board.builder()
                    .categoryIdx(1)
                    .title(shortTitle)
                    .writer("테스터")
                    .content("내용 1")
                    .password("rkskekfkakqkt!1")
                    .build();

            Board boardB = Board.builder()
                    .categoryIdx(1)
                    .title(longTitle.toString())
                    .writer("테스터")
                    .content("내용 1")
                    .password("rkskekfkakqkt!1")
                    .build();

            // when, then
            ValidationUtils.validateBoard(boardA);
            ValidationUtils.validateBoard(boardB);
        }

        @Test
        @DisplayName("4글자 미만, 100글자를 초과한 경우 예외가 발생한다.")
        void title_length_invalid() {
            // given
            String shortTitle = "가나다";
            StringBuilder longTitleSource = new StringBuilder("가나다라");
            while (longTitleSource.length() <= 100) {
                longTitleSource.append("나");
            }
            String longTitle = longTitleSource.toString();

            // when
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Board boardA = Board.builder()
                                .categoryIdx(1)
                                .title(shortTitle)
                                .writer("테스터")
                                .content("내용 1")
                                .password("rkskekfkakqkt!1")
                                .build();
                        ValidationUtils.validateBoard(boardA);
                    })
                    .withMessageMatching("제목은 4글자 이상, 100글자 이하로 입력해야 합니다.");

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Board boardB = Board.builder()
                                .categoryIdx(1)
                                .title(longTitle)
                                .writer("테스터")
                                .content("내용 1")
                                .password("rkskekfkakqkt!1")
                                .build();
                        ValidationUtils.validateBoard(boardB);
                    })
                    .withMessageMatching("제목은 4글자 이상, 100글자 이하로 입력해야 합니다.");
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
            Board boardA = Board.builder()
                    .categoryIdx(1)
                    .title("제목 1")
                    .writer(writer)
                    .content("내용 1")
                    .password("rkskekfkakqkt!1")
                    .build();
            ValidationUtils.validateBoard(boardA);
        }

        @DisplayName("3글자 미만 5글자 이상인 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"j", "bo", "가나다라마", "가나다라마바"})
        void writer_length_invalid(String writer) {

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Board boardA = Board.builder()
                                .categoryIdx(1)
                                .title("제목 1")
                                .writer(writer)
                                .content("내용 1")
                                .password("rkskekfkakqkt!1")
                                .build();
                        ValidationUtils.validateBoard(boardA);
                    })
                    .withMessageMatching("작성자는 3글자 이상, 4글자 이하로 입력해야 합니다.");

        }
    }

    @Nested
    @DisplayName("비밀번호의")
    class password {

        @Nested
        @DisplayName("길이는")
        class length_of {

            @DisplayName("4글자 미만 16글자 이상일 경우 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"r!1", "rkskekfkakqktk1!", "rkskekfkakqktk1!!", "rkskekfkakqktk1@@"})
            void writer_length_valid_exception(String password) {

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            Board board = Board.builder()
                                    .categoryIdx(1)
                                    .title("제목 1")
                                    .writer("테스터")
                                    .content("내용 1")
                                    .password(password)
                                    .build();

                            ValidationUtils.validateBoard(board);
                        })
                        .withMessageMatching("패스워드는 4글자 이상, 15글자 이하로 입력해야 합니다.");
            }

            @DisplayName("4글자 이상 16글자 미만이여야 한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rk!1", "rkskek!1", "rkskekfkakqkt!1", "!2rkskekfkakqkt"})
            void writer_length_valid(String password) {
                // given
                Board board = Board.builder()
                        .categoryIdx(1)
                        .title("제목 1")
                        .writer("테스터")
                        .content("내용 1")
                        .password(password)
                        .build();

                // when
                ValidationUtils.validateBoard(board);
            }
        }

        @Nested
        @DisplayName("필수 조건은")
        class requirement {

            @DisplayName("영문, 숫자, 특수문자가 포함되어 있어야 한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rkskekfkakqkk1!", "rkskekfk@#$!1", "rkskekfk%$#!1", "ndasn11432#@$!@"})
            void password_regex_pass(String password) {
                // given
                Board board = Board.builder()
                        .categoryIdx(1)
                        .title("제목 1")
                        .writer("테스터")
                        .content("내용 1")
                        .password(password)
                        .build();

                ValidationUtils.validateBoard(board);
            }

            @DisplayName("영문, 숫자, 특수문자가 포함되어 있지 않다면 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rkskekfkakqkk!", "rkskekfk211", "rkskekfk", "11432!#@$@"})
            void password_regex_fail(String password) {
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            Board board = Board.builder()
                                    .categoryIdx(1)
                                    .title("제목 1")
                                    .writer("테스터")
                                    .content("내용 1")
                                    .password(password)
                                    .build();

                            ValidationUtils.validateBoard(board);
                        })
                        .withMessageMatching("패스워드는 영문, 숫자, 특수문자를 포함해야 합니다.");
            }

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
                Board boardA = Board.builder()
                        .categoryIdx(1)
                        .title("제목 1")
                        .writer("테스터")
                        .content(shortContent)
                        .password("rkskekfkakqkt!1")
                        .build();

                Board boardB = Board.builder()
                        .categoryIdx(1)
                        .title("제목 1")
                        .writer("테스터")
                        .content(longContent.toString())
                        .password("rkskekfkakqkt!1")
                        .build();

                ValidationUtils.validateBoard(boardA);
                ValidationUtils.validateBoard(boardB);
            }


            @Test
            @DisplayName("4글자 미만 2000글자 이상일 경우 예외가 발생한다.")
            void content_length_invalid() {

                String shortContent = "가나다";
                StringBuilder longContent = new StringBuilder("가나다라");
                while (longContent.length() <= 2000) {
                    longContent.append("나");
                }

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            Board boardA = Board.builder()
                                    .categoryIdx(1)
                                    .title("제목 1")
                                    .writer("테스터")
                                    .content(shortContent)
                                    .password("rkskekfkakqkt!1")
                                    .build();
                            ValidationUtils.validateBoard(boardA);
                        })
                        .withMessageMatching("내용은 4글자 이상, 2000글자 이하로 입력해야 합니다.");

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            Board boardB = Board.builder()
                                    .categoryIdx(1)
                                    .title("제목 1")
                                    .writer("테스터")
                                    .content(longContent.toString())
                                    .password("rkskekfkakqkt!1")
                                    .build();
                            ValidationUtils.validateBoard(boardB);
                        })
                        .withMessageMatching("내용은 4글자 이상, 2000글자 이하로 입력해야 합니다.");
            }
        }
    }
