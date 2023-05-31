package com.study.model.board;

import com.study.ebsoft.model.board.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("비밀번호의")
public class PasswordTest {

    @Test
    void create_password() {
        // given
        String value = "password1!";
        Password password = new Password(value);

        // when
        boolean acture = password.equals(new Password(value));

        // then
        assertThat(acture).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"password1!:true", "password1@:false", "password1!$:false"}, delimiter = ':')
    void password_match(String value, boolean expected) {
        // given
        Password password = new Password("password1!");

        assertEquals(expected, password.equals(new Password(value)));
    }
    
    @Nested
    @DisplayName("길이는")
    class length {

        @DisplayName("4글자 미만일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"1", "a", "가", "12", "ab", "가나", "123", "abc", "가나다"})
        void invalid_password_shorter_than(String value) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Password(value);
                    })
                    .withMessageMatching("패스워드는 4글자 미만 16글자 이상일 수 없습니다.");
        }

        @DisplayName("16글자 이상일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"1234567891123456", "abcdefghizklmnop", "가나다라마바사아자차카파하가나다"})
        void invalid_password_more_than(String value) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Password(value);
                    })
                    .withMessageMatching("패스워드는 4글자 미만 16글자 이상일 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("필수조건은")
    class requirement {

        @DisplayName("영문, 숫자, 특수문자가 포함되어 있지 않다면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"rkskekfkakqkk!", "rkskekfk211", "rkskekfk", "11432!#@$@"})
        void password_regex_fail(String value) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Password(value);
                    })
                    .withMessageMatching("패스워드는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.");
        }
    }
}
