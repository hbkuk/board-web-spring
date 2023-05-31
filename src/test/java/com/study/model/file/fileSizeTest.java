package com.study.model.file;

import com.study.ebsoft.model.file.FileSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class fileSizeTest {

    @Test
    void create_image_size() {
        FileSize imageSize = new FileSize(1_048_576);

        assertThat(imageSize).isEqualTo(new FileSize(1_048_576));
    }

    @DisplayName("10_485_760 byte 미만이여만 한다.")
    @ParameterizedTest
    @ValueSource(ints = {1_048_576, 1_048_577, 10_485_758, 10_485_759})
    void valid_image_size(int imageSize) {
            new FileSize(imageSize);
    }

    @DisplayName("10_485_760 byte 이상일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {10_485_760, 10_485_761, 10_485_762, 12_000_000})
    void invalid_image_size(int imageSize) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new FileSize(imageSize);
                })
                .withMessageMatching("이미지의 크기가 10_485_760 byte 이상일 수 없습니다.");

    }
}
