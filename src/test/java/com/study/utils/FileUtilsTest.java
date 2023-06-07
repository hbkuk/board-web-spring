package com.study.utils;

import com.study.ebsoft.domain.File;
import com.study.ebsoft.utils.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FileUtilsTest {

    @Mock
    java.io.File file;

    @BeforeEach
    public void beforeClass() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deleteFileFromServerDirectory() {

        MockedStatic<FileUtils> mFileUtils = mockStatic(FileUtils.class);

        File fileA = File.builder()
                .savedName("test.jpg")
                .originalName("test.jpg")
                .fileSize(1_048_576)
                .boardIdx(1L)
                .build();

        when(FileUtils.deleteFileFromServerDirectory(fileA)).thenReturn(true);

        assertThat(FileUtils.deleteFileFromServerDirectory(fileA)).isEqualTo(true);

        mFileUtils.close();
    }


    @Test
    public void deleteFilesFromServerDirectory() {
        MockedStatic<FileUtils> mFileUtils = mockStatic(FileUtils.class);

        List<File> files = new ArrayList<>();
        File fileA = File.builder()
                .savedName("test.jpg")
                .originalName("test.jpg")
                .fileSize(1_048_576)
                .boardIdx(1L)
                .build();
        files.add(fileA);

        FileUtils.deleteFilesFromServerDirectory(files);

        verify(file, times(1)).delete();

        mFileUtils.close();
    }
}

