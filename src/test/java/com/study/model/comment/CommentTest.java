package com.study.model.comment;

import com.study.ebsoft.model.board.BoardIdx;
import com.study.ebsoft.model.board.Password;
import com.study.ebsoft.model.comment.Comment;
import com.study.ebsoft.model.comment.CommentContent;
import com.study.ebsoft.model.comment.CommentWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("comment 도메인의")
public class CommentTest {

    @Test
    void create() {
        Comment comment = new Comment.Builder()
                .writer(new CommentWriter("테스터"))
                .password(new Password("rkskekfka1!"))
                .content(new CommentContent("내용 1"))
                .boardIdx(new BoardIdx(1))
                .build();
    }

    @Nested
    @DisplayName("작성자는")
    class writer {

        @DisplayName("3글자 이상 5글자 미만이여야 한다.")
        @ParameterizedTest
        @ValueSource(strings = {"bob", "jany", "테스터", "내이름은"})
        void writer_length_valid(String writer) {
            // given
            new Comment.Builder()
                    .writer(new CommentWriter(writer))
                    .password(new Password("rkskekfka1!"))
                    .content(new CommentContent("내용 1"))
                    .boardIdx(new BoardIdx(1))
                    .build();
        }

        @DisplayName("3글자 미만 5글자 이상인 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"j", "bo", "가나다라마", "가나다라마바"})
        void writer_length_invalid(String writer) {

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Comment.Builder()
                                .writer(new CommentWriter(writer))
                                .password(new Password("rkskekfka1!"))
                                .content(new CommentContent("내용 1"))
                                .boardIdx(new BoardIdx(1))
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
                new Comment.Builder()
                        .writer(new CommentWriter("테스터"))
                        .password(new Password(password))
                        .content(new CommentContent("내용 1"))
                        .boardIdx(new BoardIdx(1))
                        .build();
            }
        }

        @DisplayName("4글자 미만 16글자 이상일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"r!1", "rkskekfkakqktk1!", "rkskekfkakqktk1!!", "rkskekfkakqktk1@@"})
        void writer_length_valid(String password) {

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Comment.Builder()
                                .writer(new CommentWriter("테스터"))
                                .password(new Password(password))
                                .content(new CommentContent("내용 1"))
                                .boardIdx(new BoardIdx(1))
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
                new Comment.Builder()
                        .writer(new CommentWriter("테스터"))
                        .password(new Password(password))
                        .content(new CommentContent("내용 1"))
                        .boardIdx(new BoardIdx(1))
                        .build();
            }

            @DisplayName("영문, 숫자, 특수문자가 포함되어 있지 않다면 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"rkskekfkakqkk!", "rkskekfk211", "rkskekfk", "11432!#@$@"})
            void password_regex_fail(String password) {
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            new Comment.Builder()
                                    .writer(new CommentWriter("테스터"))
                                    .password(new Password(password))
                                    .content(new CommentContent("내용 1"))
                                    .boardIdx(new BoardIdx(1))
                                    .build();})
                        .withMessageMatching("패스워드는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.");
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
            new Comment.Builder()
                    .writer(new CommentWriter("테스터"))
                    .password(new Password("rkskekfka1!"))
                    .content(new CommentContent(shortContent))
                    .boardIdx(new BoardIdx(1))
                    .build();

            new Comment.Builder()
                    .writer(new CommentWriter("테스터"))
                    .password(new Password("rkskekfka1!"))
                    .content(new CommentContent(longContent.toString()))
                    .boardIdx(new BoardIdx(1))
                    .build();
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
                        new Comment.Builder()
                                .writer(new CommentWriter("테스터"))
                                .password(new Password("rkskekfka1!"))
                                .content(new CommentContent(shortContent))
                                .boardIdx(new BoardIdx(1))
                                .build();})
                    .withMessageMatching("내용은 4글자 미만 1000글자를 초과할 수 없습니다.");

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Comment.Builder()
                                .writer(new CommentWriter("테스터"))
                                .password(new Password("rkskekfka1!"))
                                .content(new CommentContent(longContent.toString()))
                                .boardIdx(new BoardIdx(1))
                                .build();})
                    .withMessageMatching("내용은 4글자 미만 1000글자를 초과할 수 없습니다.");
        }
    }
}
