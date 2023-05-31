package com.study.ebsoft.utils;

import com.oreilly.servlet.MultipartRequest;
import com.study.ebsoft.model.board.*;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.model.file.FileOriginalName;
import com.study.ebsoft.model.file.FileSize;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
    public static Board buildWriteBoardFromRequest(MultipartRequest request) {
        return new Board.Builder()
                .categoryIdx(new CategoryIdx(Integer.parseInt(request.getParameter(CATEGORY_IDX_PARAMETER_NAME))))
                .title(new Title(request.getParameter(TITLE_PARAMETER_NAME)))
                .writer(new BoardWriter(request.getParameter(WRITER_PARAMETER_NAME)))
                .content(new BoardContent(request.getParameter(CONTENT_PARAMETER_NAME)))
                .password(new Password(request.getParameter(PASSWORD_PARAMETER_NAME)))
                .build();
    }

    /**
     * 게시글 수정에 필요한 파라미터를 추출해서 Board 도메인 객체를 생성하고 반환합니다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return Board 객체를 반환
     */
    public static Board buildModifyBoardFromRequest(MultipartRequest request) {
        return new Board.Builder()
                .boardIdx(new BoardIdx(Long.parseLong(request.getParameter(BOARD_IDX_PARAMETER_NAME))))
                .categoryIdx(new CategoryIdx(Integer.parseInt(request.getParameter(CATEGORY_IDX_PARAMETER_NAME))))
                .title(new Title(request.getParameter(TITLE_PARAMETER_NAME)))
                .writer(new BoardWriter(request.getParameter(WRITER_PARAMETER_NAME)))
                .content(new BoardContent(request.getParameter(CONTENT_PARAMETER_NAME)))
                .password(new Password(request.getParameter(PASSWORD_PARAMETER_NAME)))
                .build();
    }

    /**
     * 파일 저장에 필요한 파라미터를 추출해서 File 도메인 객체 리스트를 생성하고 반환합니다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return File 객체 리스트를 반환
     */
    public static List<File> buildFilesFromRequest(MultipartRequest request) {
        List<File> files = new ArrayList<>();
        Enumeration fileNames = request.getFileNames();

        while (fileNames.hasMoreElements()) {
            String fileName = (String) fileNames.nextElement();

            String fileSystemName = request.getFilesystemName(fileName);
            String originalFileName = request.getOriginalFileName(fileName);

            if (originalFileName != null && fileSystemName != null) {
                files.add(new File.Builder()
                            .saveFileName(fileSystemName)
                            .originalName(new FileOriginalName(originalFileName))
                            .fileSize(new FileSize((int) FileUtils.getFileSize(fileSystemName)))
                            .build());
            }
        }
        return files;
    }
}
