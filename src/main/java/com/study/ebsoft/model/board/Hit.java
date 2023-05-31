package com.study.ebsoft.model.board;

import java.util.Objects;

public class Hit {
    private static final int MIN_HIT_VALUE = 0;

    private int hit = 0;

    public Hit(int hit) {
        if (hit < MIN_HIT_VALUE) {
            throw new IllegalArgumentException("조회수는 음수일 수 없습니다.");
        }
        this.hit = hit;
    }

    public Hit increase() {
        return new Hit(this.hit + 1);
    }

    public int getHit() {
        return hit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Hit hit1 = (Hit) o;
        return hit == hit1.hit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hit);
    }
}
