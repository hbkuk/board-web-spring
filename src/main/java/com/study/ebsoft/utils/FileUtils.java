package com.study.ebsoft.utils;

import com.study.ebsoft.utils.validation.FileValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 게시글 작성 또는 수정시
 *      서버 디렉토리에 파일을 저장 및 삭제하는 유틸 클래스
 */
@Slf4j
public class FileUtils {

    public static final String UPLOAD_PATH = "C:\\upload\\";

    /**
     * 파일 이름에 해당하는 파일을 삭제했다면 true, 그렇지 않다면 false 를 반환합니다.
     *
     * @param fileName 삭제할 파일이름
     * @return 파일을 삭제했다면 true, 그렇지 않다면 false
     */
    public static boolean deleteUploadedFile(String fileName) {
        log.debug("삭제할 File : {}{}{} ", UPLOAD_PATH,"\\",fileName);
        return createFile(fileName).delete();
    }

    public static File createFile(String fileName) {
        return new File(UPLOAD_PATH, fileName);
    }

    public static String generateEncodedName(com.study.ebsoft.domain.File file) {
        try {
            return URLEncoder.encode(file.getOriginalName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error(String.valueOf(e));
        }
        return null;
    }

    public static byte[] convertByteArray(String savedFileName) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(FileUtils.createFile(savedFileName));

            byte[] buffer = new byte[1024];
            int bytesRead;

            byteArrayOutputStream = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error(String.valueOf(e));
        } finally {
            if(byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    log.error(String.valueOf(e));
                }
            }
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(String.valueOf(e));
                }
            }
        }
        return null;
    }

    // TODO: javadoc
    public static List<com.study.ebsoft.domain.File> toFilesAfterUpload(MultipartFile[] multipartFiles, Long boardIdx) {
        List<com.study.ebsoft.domain.File> files = new ArrayList<>();

        if (hasExistFile(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    String fileName = multipartFile.getOriginalFilename();
                    int fileSize = (int) multipartFile.getSize();
                    String systemName = generateSystemName(fileName);

                    try {
                        multipartFile.transferTo(createAbsolutePath(systemName));
                        log.debug("업로드 완료 .. 저장된 파일 이름 : {} ", systemName);
                    } catch (IOException e) {
                        // 파일 업로드 중 에러 발생 시 파일 삭제
                        log.error("파일 업로드 중 예외 발생 -> error : {}", e.getMessage());

                        for (com.study.ebsoft.domain.File file : files) {
                            deleteFileFromServerDirectory(file);
                        }
                    }

                    com.study.ebsoft.domain.File file = com.study.ebsoft.domain.File.builder()
                            .savedName(systemName)
                            .originalName(fileName)
                            .fileSize(fileSize)
                            .boardIdx(boardIdx)
                            .build();
                    files.add(file);
                }
            }
        }
        return files;
    }

    public static boolean deleteFileFromServerDirectory(com.study.ebsoft.domain.File file) {
        return FileUtils.deleteUploadedFile(file.getSavedName());
    }

    public static void deleteFilesFromServerDirectory(List<com.study.ebsoft.domain.File> files ) {
        for (com.study.ebsoft.domain.File file : files) {
            deleteUploadedFile(file.getSavedName());
        }
    }

    public static Path createAbsolutePath(String systemName) {
        Path path = Paths.get(FileUtils.UPLOAD_PATH + systemName);
        return path;
    }

    public static String generateSystemName(String fileName) {
        return String.format("%s.%s", UUID.randomUUID(), FileValidationUtils.extractFileExtension(fileName));
    }

    public static boolean hasExistFile(MultipartFile[] multipartFiles) {
        return multipartFiles != null && multipartFiles.length > 0;
    }
}
