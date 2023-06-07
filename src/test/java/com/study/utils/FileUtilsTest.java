package com.study.utils;

import com.study.ebsoft.domain.File;
import com.study.ebsoft.utils.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class FileUtilsTest {

    private static MockedStatic<FileUtils> mFileUtils;

    @BeforeAll
    public static void beforeClass() {
        mFileUtils = mockStatic(FileUtils.class);
    }

    @AfterAll
    public static void afterClass() {
        mFileUtils.close();
    }

    @Test
    public void testDeleteUploadedFile() {
        File fileA = File.builder()
                .savedName("test.jpg")
                .originalName("test.jpg")
                .fileSize(1_048_576)
                .boardIdx(1L)
                .build();

        when(FileUtils.deleteFileFromServerDirectory(fileA)).thenReturn(true);

        assertThat(FileUtils.deleteFileFromServerDirectory(fileA)).isEqualTo(true);
    }
}

