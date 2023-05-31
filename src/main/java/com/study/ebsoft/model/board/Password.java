package com.study.ebsoft.model.board;

import java.util.Objects;
import java.util.regex.Pattern;

public class Password {

    private static final int MIN_PASSWORD_LENGTH_VALUE = 4;
    private static final int MAX_PASSWORD_LENGTH_VALUE = 15;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$";
    private String password;

    public Password(String password) {
        if (isInvalidLength(password)) {
            throw new IllegalArgumentException("패스워드는 4글자 미만 16글자 이상일 수 없습니다.");
        }

        if (isInvalidFormat(password)) {
            throw new IllegalArgumentException("패스워드는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.");
        }

        this.password = password;
    }

    private boolean isInvalidFormat(String password) {
        return !getPatternCompile().matcher(password).matches();
    }

    private boolean isInvalidLength(String password) {
        return password.length() < MIN_PASSWORD_LENGTH_VALUE || password.length() > MAX_PASSWORD_LENGTH_VALUE;
    }

    private Pattern getPatternCompile() {
        return Pattern.compile(PASSWORD_PATTERN);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
