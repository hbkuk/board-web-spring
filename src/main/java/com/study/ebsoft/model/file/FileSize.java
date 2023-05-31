package com.study.ebsoft.model.file;

import java.util.Objects;

public class FileSize {

    private static final int MAX_IMAGE_SIZE_VALUE = 10_485_760;
    private int imageSize;

    public FileSize(int imageSize) {
        if (imageSize >= MAX_IMAGE_SIZE_VALUE) {
            throw new IllegalArgumentException("이미지의 크기가 10_485_760 byte 이상일 수 없습니다.");
        }
        this.imageSize = imageSize;
    }

    public int getImageSize() {
        return imageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FileSize imageSize1 = (FileSize) o;
        return imageSize == imageSize1.imageSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageSize);
    }
}
