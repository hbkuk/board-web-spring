package com.study.model.file;

import com.study.ebsoft.model.board.BoardIdx;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.model.file.FileOriginalName;
import com.study.ebsoft.model.file.FileSize;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("이미지의 ")
public class fileTest {

    @Test
    void create_image() {
        File file = new File.Builder()
                .originalName(new FileOriginalName("test.jpg"))
                .fileSize(new FileSize(127904))
                .boardIdx(new BoardIdx(1))
                .build();
    }

    @Nested
    @DisplayName("이름은 ")
    class name {

        @DisplayName("유효한(png, jpeg, bmp 등) 확장자여야만 한다.")
        @ParameterizedTest
        @ValueSource(strings = {"test.png", "test.jpeg", "test.bmp", "test.gif", "test.jpg"})
        void valid_image_name_extension(String name) {
            File image = new File.Builder()
                    .originalName(new FileOriginalName(name))
                    .fileSize(new FileSize(127904))
                    .boardIdx(new BoardIdx(1))
                    .build();
        }

        @DisplayName("유효하지 않은 확장자일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"test.exe", "test.com", "test.bat", "test.ti", "test.abc"})
        void invalid_image_name_extension(String name) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new File.Builder()
                                .originalName(new FileOriginalName(name))
                                .fileSize(new FileSize(127904))
                                .boardIdx(new BoardIdx(1))
                                .build();
                    })
                    .withMessageMatching("유효하지 않은 확장자입니다.");
        }
    }

    @Disabled
    @Nested
    @DisplayName("크기는")
    class size {

        @DisplayName("10_485_760 byte 미만이여만 한다.")
        @ParameterizedTest
        @ValueSource(ints = {1_048_576, 1_048_577, 10_485_758, 10_485_759})
        void valid_image_size(int imageSize) {
            File image = new File.Builder()
                    .originalName(new FileOriginalName("test.png"))
                    .fileSize(new FileSize(imageSize))
                    .boardIdx(new BoardIdx(1))
                    .build();
        }

        @DisplayName("10_485_760 byte 이상일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {10_485_760, 10_485_761, 10_485_762, 12_000_000})
        void invalid_image_size(int imageSize) {

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        File image = new File.Builder()
                                .originalName(new FileOriginalName("test.png"))
                                .fileSize(new FileSize(imageSize))
                                .boardIdx(new BoardIdx(1))
                                .build();
                    })
                    .withMessageMatching("이미지의 크기가 10_485_760 byte 이상일 수 없습니다.");

        }

    }
}
