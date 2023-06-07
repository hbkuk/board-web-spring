package com.study.utils;

import com.study.ebsoft.domain.File;
import com.study.ebsoft.utils.validation.FileValidationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@DisplayName("파일의 ")
public class FileValidationUtilsTest {

    @Test
    void create_file() {
        File file = File.builder()
                .savedName("test.jpg")
                .originalName("test.jpg")
                .fileSize(127904)
                .boardIdx(1L)
                .build();

        FileValidationUtils.validateOnCreate(file);
    }

    @Nested
    @DisplayName("파일 이름의")
    class name {

        @Nested
        @DisplayName("확장자는")
        class extension {


            @DisplayName("유효한(png, jpeg, bmp 등) 확장자여야만 한다.")
            @ParameterizedTest
            @ValueSource(strings = {"test.png", "test.jpeg", "test.bmp", "test.gif", "test.jpg"})
            void valid_image_name_extension(String name) {
                File file = File.builder()
                        .savedName(name)
                        .originalName(name)
                        .fileSize(127904)
                        .boardIdx(1L)
                        .build();

                FileValidationUtils.validateOnCreate(file);
            }

            @DisplayName("유효하지 않은 확장자일 경우 예외가 발생한다.")
            @ParameterizedTest
            @ValueSource(strings = {"test.exe", "test.com", "test.bat", "test.ti", "test.abc"})
            void invalid_image_name_extension(String name) {
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            File file = File.builder()
                                    .savedName(name)
                                    .originalName(name)
                                    .fileSize(127904)
                                    .boardIdx(1L)
                                    .build();
                            FileValidationUtils.validateOnCreate(file);
                        })
                        .withMessageMatching("유효하지 않은 확장자입니다.");
            }
        }

        @Nested
        @DisplayName("길이는")
        class length {

            @Test
            @DisplayName("5글자 이상 255글자 미만이여야 한다.")
            void file_name_length_valid() {
                // given
                String shortContent = "가.jpg";
                StringBuilder longContent = new StringBuilder("가나다라");
                while (longContent.length() < 251) {
                    longContent.append("나");
                }
                longContent.append(".jpg");

                // when
                File fileA = File.builder()
                        .savedName(shortContent)
                        .originalName(shortContent)
                        .fileSize(127904)
                        .boardIdx(1L)
                        .build();

                File fileB = File.builder()
                        .savedName(longContent.toString())
                        .originalName(longContent.toString())
                        .fileSize(127904)
                        .boardIdx(1L)
                        .build();

                FileValidationUtils.validateOnCreate(fileA);
                FileValidationUtils.validateOnCreate(fileB);
            }

            @Test
            @DisplayName("5글자 미만 255글자 이상일 경우 예외가 발생한다.")
            void file_name_length_invalid() {

                String shortContent = ".jpg";
                StringBuilder longContent = new StringBuilder("가나다라");
                while (longContent.length() <= 252) {
                    longContent.append("나");
                }
                longContent.append(".jpg");

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            File fileA = File.builder()
                                    .savedName(shortContent)
                                    .originalName(shortContent)
                                    .fileSize(127904)
                                    .boardIdx(1L)
                                    .build();
                            FileValidationUtils.validateOnCreate(fileA);
                        })
                        .withMessageMatching("파일의 이름은 5글자 이상, 255글자 이하여야만 합니다.");

                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            File fileA = File.builder()
                                    .savedName(longContent.toString())
                                    .originalName(longContent.toString())
                                    .fileSize(127904)
                                    .boardIdx(1L)
                                    .build();
                            FileValidationUtils.validateOnCreate(fileA);
                        })
                        .withMessageMatching("파일의 이름은 5글자 이상, 255글자 이하여야만 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("크기는")
    class size {

        @DisplayName("10_485_760 byte 미만이여만 한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 1_048_576, 1_048_577, 10_485_758, 10_485_759})
        void valid_file_size(int fileSize) {
            File file = File.builder()
                    .savedName("test.jpg")
                    .originalName("test.jpg")
                    .fileSize(fileSize)
                    .boardIdx(1L)
                    .build();
            FileValidationUtils.validateOnCreate(file);
        }

        @DisplayName("10_485_760 byte 이상일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, 10_485_761, 10_485_762, 12_000_000})
        void invalid_file_size(int fileSize) {

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        File file = File.builder()
                                .savedName("test.jpg")
                                .originalName("test.jpg")
                                .fileSize(fileSize)
                                .boardIdx(1L)
                                .build();
                        FileValidationUtils.validateOnCreate(file);
                    })
                    .withMessageMatching("파일의 크기는 1 이상, 10485760 byte 이하여야만 합니다.");
        }
    }
}
