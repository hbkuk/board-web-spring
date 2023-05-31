package com.study.ebsoft.utils;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 게시글 작성 또는 수정시
 *      서버 디렉토리에 파일을 저장 및 삭제하는 유틸 클래스
 */
@Slf4j
public class FileUtils {

    public static final String UPLOAD_PATH = "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\upload";
    private static final int MAX_FILE_SIZE = 2 * 1024 * 1024;

    /**
     * HttpServletRequest 를 인자로 받아 MultipartRequest 생성하고 리턴합니다.
     *
     * @param request HttpServletRequest 객체
     * @return MultipartRequest 객체
     */
    public static MultipartRequest fileUpload(HttpServletRequest request) {

        try {
            MultipartRequest multi = new MultipartRequest(
                    request,
                    UPLOAD_PATH,
                    MAX_FILE_SIZE,
                    "utf-8",
                    new DefaultFileRenamePolicy());

            return multi;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 파일 이름에 해당하는 파일을 삭제했다면 true, 그렇지 않다면 false 를 반환합니다.
     *
     * @param fileName 삭제할 파일이름
     * @return 파일을 삭제했다면 true, 그렇지 않다면 false
     */
    public static boolean deleteUploadedFile(String fileName) {
        log.debug("삭제할 File : {}{}{} ", UPLOAD_PATH,"\\",fileName);
        File file = new File(UPLOAD_PATH, fileName);
        return file.delete();
    }

    /**
     * 디렉토리 내에서 해당 파일의 크기를 반환합니다.
     *
     * @param fileName 파일의 이름
     * @return 파일의 크기를 반환
     */
    public static long getFileSize(String fileName) {
        File file = new File(
                FileUtils.UPLOAD_PATH, fileName);
        return file.length();
    }

    /**
     * 웹 브라우저로부터 요청된 파일이름을 디렉토리 내에서 읽고, 웹 브라우저에 응답합니다.
     * 
     * @param request 요청 정보를 가지고 있는 객체
     * @param response 응답 정보를 가지고 있는 객체
     * @param savedFileName 디렉토리에 저장된 파일 이름
     * @param originalFileName 클라이언트가 알고 있는 파일 이름
     */
    public static void serveDownloadFile(HttpServletRequest request, HttpServletResponse response, String savedFileName, String originalFileName) throws IOException {
        InputStream in = null;
        OutputStream os = null;
        File file = null;
        boolean skip = false;
        String client = "";

        try {
            try {
                file = new File(UPLOAD_PATH, savedFileName);
                in = new FileInputStream(file);
            } catch (FileNotFoundException fe) {
                skip = true;
            }

            client = request.getHeader("User-Agent");

            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Description", "File Download");

            if (!skip) {
                if (client.indexOf("MSIE") != -1) {
                    response.setHeader("Content-Disposition", "attachment; filename=" + new String(originalFileName.getBytes("KSC5601"), "ISO8859_1"));
                } else {
                    originalFileName = new String(originalFileName.getBytes("utf-8"), "iso-8859-1");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFileName + "\"");
                    response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
                }

                response.setHeader("Content-Length", String.valueOf(file.length()));

                os = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            } else {
                response.setContentType("text/html;charset=UTF-8");
            }
            in.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
            os.close();
        }
    }
}
