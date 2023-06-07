package com.study.domain;

import com.study.ebsoft.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @Test
    @DisplayName("패스워가 같다면 true를 리턴한다.")
    void same_password() {

        // given
        String password = "rkskekfka1!";

        Comment comment = Comment.builder()
                .writer("테스터")
                .password(password)
                .content("내용 1")
                .boardIdx(1L)
                .build();

        // when
        boolean actual = comment.isSamePassword(password);

        // then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("패스워가 다르다면 false를 리턴한다.")
    void not_same_password() {

        // given
        String password = "rkskekfka1!";
        String anotherPassword = "rkskekf1!";

        Comment comment = Comment.builder()
                .writer("테스터")
                .password(password)
                .content("내용 1")
                .boardIdx(1L)
                .build();

        // when
        boolean actual = comment.isSamePassword(anotherPassword);

        // then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("패스워드가 같다면 canDelete() 메서드는 true를 리턴한다.")
    void can_delete() {
        // 동일한 패스워드
        String password = "rkskekfka1!";
        String anotherPassword = "rkskekfka1!";

        Comment comment = Comment.builder()
                .writer("테스터")
                .password(password)
                .content("내용 1")
                .boardIdx(1L)
                .build();

        boolean actual = comment.canDelete(anotherPassword);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("패스워드가 다르다면 canDelete() 메서드는 false를 리턴한다.")
    void cant_delete() {
        // 동일한 패스워드
        String password = "rkskekfka1!";
        String anotherPassword = "rkdskatmxk@@@111";

        Comment comment = Comment.builder()
                .writer("테스터")
                .password(password)
                .content("내용 1")
                .boardIdx(1L)
                .build();

        boolean actual = comment.canDelete(anotherPassword);

        assertThat(actual).isEqualTo(false);
    }
}
