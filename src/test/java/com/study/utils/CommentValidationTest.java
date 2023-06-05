package com.study.utils;

import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.utils.validation.CommentValidationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@DisplayName("comment 도메인의")
public class CommentValidationTest {

    @Test
    void create() {
        Comment comment = Comment.builder()
                .writer("테스터")
                .password("rkskekfka1!")
                .content("내용 1")
                .boardIdx(1L)
                .build();

        CommentValidationUtils.create(comment);
    }

    @Nested
    @DisplayName("작성자는")
    class writer {

        @DisplayName("3글자 이상 5글자 미만이여야 한다.")
        @ParameterizedTest
        @ValueSource(strings = {"bob", "jany", "테스터", "내이름은"})
        void writer_length_valid(String writer) {
            // given
            Comment comment = Comment.builder()
                    .writer(writer)
                    .password("rkskekfka1!")
                    .content("내용 1")
                    .boardIdx(1L)
                    .build();
            CommentValidationUtils.create(comment);
        }

        @DisplayName("3글자 미만 5글자 이상인 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"j", "bo", "가나다라마", "가나다라마바"})
        void writer_length_invalid(String writer) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Comment comment = Comment.builder()
                                .writer(writer)
                                .password("rkskekfka1!")
                                .content("내용 1")
                                .boardIdx(1L)
                                .build();
                        CommentValidationUtils.create(comment);
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

            @DisplayName("4글자 미만 15글자 이상일 경우 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"r!1", "rkskekfkakqktk1!", "rkskekfkakqktk1!!", "rkskekfkakqktk1@@"})
            void writer_length_valid_exception(String password) {
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            Comment comment = Comment.builder()
                                    .writer("테스터")
                                    .password(password)
                                    .content("내용 1")
                                    .boardIdx(1L)
                                    .build();
                            CommentValidationUtils.create(comment);
                        })
                        .withMessageMatching("패스워드는 4글자 이상, 15글자 이하로 입력해야 합니다.");
            }

            @DisplayName("4글자 이상 15글자 미만이여야 한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rk!1", "rkskek!1", "rkskekfkakqkt!1", "!2rkskekfkakqkt"})
            void writer_length_valid(String password) {
                // given
                Comment comment = Comment.builder()
                        .writer("테스터")
                        .password(password)
                        .content("내용 1")
                        .boardIdx(1L)
                        .build();
                CommentValidationUtils.create(comment);
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
                Comment comment = Comment.builder()
                        .writer("테스터")
                        .password(password)
                        .content("내용 1")
                        .boardIdx(1L)
                        .build();

                CommentValidationUtils.create(comment);
            }

            @DisplayName("영문, 숫자, 특수문자가 포함되어 있지 않다면 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rkskekfkakqkk!", "rkskekfk211", "rkskekfk", "11432!#@$@"})
            void password_regex_fail(String password) {
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            // given
                            Comment comment = Comment.builder()
                                    .writer("테스터")
                                    .password(password)
                                    .content("내용 1")
                                    .boardIdx(1L)
                                    .build();
                            CommentValidationUtils.create(comment);
                        })
                        .withMessageMatching("패스워드는 영문, 숫자, 특수문자를 포함해야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("내용은")
    class content {

        @Test
        @DisplayName("4글자 이상 1000글자 미만이여야 한다.")
        void content_length_valid() {
            // given
            String shortContent = "가나다라";
            StringBuilder longContent = new StringBuilder("가나다라");
            while (longContent.length() < 999) {
                longContent.append("나");
            }

            // when
            Comment commentA = Comment.builder()
                    .writer("테스터")
                    .password("rkskekfka1!")
                    .content(shortContent)
                    .boardIdx(1L)
                    .build();

            CommentValidationUtils.create(commentA);

            Comment commentB = Comment.builder()
                    .writer("테스터")
                    .password("rkskekfka1!")
                    .content(longContent.toString())
                    .boardIdx(1L)
                    .build();

            CommentValidationUtils.create(commentB);
        }

        @Test
        @DisplayName("4글자 미만 1000글자 이상일 경우 예외가 발생한다.")
        void content_length_invalid() {

            String shortContent = "가나다";
            StringBuilder longContent = new StringBuilder("가나다라");
            while (longContent.length() <= 1000) {
                longContent.append("나");
            }

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Comment comment = Comment.builder()
                                .writer("테스터")
                                .password("rkskekfka1!")
                                .content(shortContent)
                                .boardIdx(1L)
                                .build();
                        CommentValidationUtils.create(comment);
                    })
                    .withMessageMatching("내용은 4글자 이상, 1000글자 이하로 입력해야 합니다.");

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Comment comment = Comment.builder()
                                .writer("테스터")
                                .password("rkskekfka1!")
                                .content(longContent.toString())
                                .boardIdx(1L)
                                .build();
                        CommentValidationUtils.create(comment);
                    })
                    .withMessageMatching("내용은 4글자 이상, 1000글자 이하로 입력해야 합니다.");
        }
    }
}
