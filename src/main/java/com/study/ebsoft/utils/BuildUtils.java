package com.study.ebsoft.utils;

import com.oreilly.servlet.MultipartRequest;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.FileDTO;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 게시글 작성 또는 수정시
 * 도메인 객체를 생성하는 유틸 클래스
 */
public class BuildUtils {

    private static final String CATEGORY_IDX_PARAMETER_NAME = "category_idx";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String WRITER_PARAMETER_NAME = "writer";
    private static final String CONTENT_PARAMETER_NAME = "content";
    private static final String PASSWORD_PARAMETER_NAME = "password";
    private static final String BOARD_IDX_PARAMETER_NAME = "board_idx";

    /**
     * 게시글 작성에 필요한 파라미터를 추출해서 도메인 객체를 Board 생성하고 반환합니다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return Board 객체를 반환
     */
    public static BoardDTO buildWriteBoardFromRequest(MultipartRequest request) {
        boardValidation(request);
        return BoardDTO.builder()
                .categoryIdx(Integer.parseInt(request.getParameter(CATEGORY_IDX_PARAMETER_NAME)))
                .title(request.getParameter(TITLE_PARAMETER_NAME))
                .writer(request.getParameter(WRITER_PARAMETER_NAME))
                .content(request.getParameter(CONTENT_PARAMETER_NAME))
                .password(request.getParameter(PASSWORD_PARAMETER_NAME))
                .build();
    }

    /**
     * 게시글 수정에 필요한 파라미터를 추출해서 Board 도메인 객체를 생성하고 반환합니다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return Board 객체를 반환
     */
    public static BoardDTO buildModifyBoardFromRequest(MultipartRequest request) {
        boardValidation(request);
        return BoardDTO.builder()
                .boardIdx(Long.parseLong(request.getParameter(BOARD_IDX_PARAMETER_NAME)))
                .categoryIdx(Integer.parseInt(request.getParameter(CATEGORY_IDX_PARAMETER_NAME)))
                .title(request.getParameter(TITLE_PARAMETER_NAME))
                .writer(request.getParameter(WRITER_PARAMETER_NAME))
                .content(request.getParameter(CONTENT_PARAMETER_NAME))
                .password(request.getParameter(PASSWORD_PARAMETER_NAME))
                .build();
    }

    /**
     * 파일 저장에 필요한 파라미터를 추출해서 File 도메인 객체 리스트를 생성하고 반환합니다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return File 객체 리스트를 반환
     */
    public static List<FileDTO> buildFilesFromRequest(MultipartRequest request) {
        List<FileDTO> files = new ArrayList<>();
        Enumeration fileNames = request.getFileNames();

        while (fileNames.hasMoreElements()) {
            String fileName = (String) fileNames.nextElement();

            String fileSystemName = request.getFilesystemName(fileName);
            String originalFileName = request.getOriginalFileName(fileName);

            if (originalFileName != null && fileSystemName != null) {
                files.add(FileDTO.builder()
                        .savedFileName(fileSystemName)
                        .originalFileName(originalFileName)
                        .fileSize((int) FileUtils.getFileSize(fileSystemName))
                        .build());
            }
        }
        return files;
    }

    private static void boardValidation(MultipartRequest request) {
        if (request.getParameter(BOARD_IDX_PARAMETER_NAME) != null) {
            if (Long.parseLong(request.getParameter(BOARD_IDX_PARAMETER_NAME)) < 0) {
                throw new IllegalArgumentException("글 번호는 음수일 수 없습니다.");
            }
        }

        if (request.getParameter(CATEGORY_IDX_PARAMETER_NAME) != null) {
            if (Integer.parseInt(request.getParameter(CATEGORY_IDX_PARAMETER_NAME)) < 0) {
                throw new IllegalArgumentException("카테고리 번호는 음수일 수 없습니다.");
            }
        }

        if (request.getParameter(TITLE_PARAMETER_NAME) != null) {
            if (request.getParameter(TITLE_PARAMETER_NAME).length() < 4 || request.getParameter(TITLE_PARAMETER_NAME).length() > 99) {
                throw new IllegalArgumentException("제목은 4글자 미만, 99글자 이상을 입력할 수 없습니다.");
            }
        }

        if (request.getParameter(WRITER_PARAMETER_NAME) != null) {
            if (request.getParameter(WRITER_PARAMETER_NAME).length() < 3 || request.getParameter(WRITER_PARAMETER_NAME).length() > 4) {
                throw new IllegalArgumentException("작성자를 3글자 미만 5글자 이상을 입력할 수 없습니다.");
            }
        }

        if (request.getParameter(CONTENT_PARAMETER_NAME) != null) {
            if (request.getParameter(CONTENT_PARAMETER_NAME).length() < 4 || request.getParameter(CONTENT_PARAMETER_NAME).length() > 1999) {
                throw new IllegalArgumentException("내용은 4글자 미만 2000글자를 초과할 수 없습니다.");
            }
        }

        if (request.getParameter(PASSWORD_PARAMETER_NAME) != null) {
            if (request.getParameter(PASSWORD_PARAMETER_NAME).length() < 4 || request.getParameter(PASSWORD_PARAMETER_NAME).length() > 15) {
                throw new IllegalArgumentException("패스워드는 4글자 미만 16글자 이상일 수 없습니다.");
            }
            if (!Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$").matcher(request.getParameter(PASSWORD_PARAMETER_NAME)).matches()) {
                throw new IllegalArgumentException("패스워드는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.");
            }
        }

    }
}
