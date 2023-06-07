package com.study.ebsoft.controller;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Category;
import com.study.ebsoft.domain.File;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.service.CategoryService;
import com.study.ebsoft.service.CommentService;
import com.study.ebsoft.service.FileService;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.validation.BoardValidationUtils;
import com.study.ebsoft.utils.validation.FileValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController()
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final FileService fileService;

    @Autowired
    public BoardController(BoardService boardService, CommentService commentService, CategoryService categoryService, FileService fileService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.categoryService = categoryService;
        this.fileService = fileService;
    }

    // TODO: 검색조건 동적쿼리

    /**
     * 검색조건(searchConditionQueryString)에 맞는 전체 게시물 리스트와 View를 응답합니다.
     */
    @GetMapping("/boards")
    public ResponseEntity<Object> findBoards(Map<String, Object> response) {
        //mav.addObject("boards", boardService.selectBoardsWithFileCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap())));
        log.debug("findBoards 호출");

        response.put("boards", boardService.findAll());
        response.put("files", fileService.findAll());
        response.put("categories", categoryService.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     */
    @GetMapping("/board/{boardIdx}")
    public ResponseEntity<Object> findBoard(@PathVariable("boardIdx") Long boardIdx, Map<String, Object> response) {
        log.debug("findBoard 호출 -> 게시글 번호 : {}", boardIdx);

        response.put("boards", boardService.findByBoardIdx(boardIdx));
        response.put("files", fileService.findAll());
        response.put("comments", commentService.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * 게시글 작성에 필요한 정보 응답합니다
     */
    @GetMapping("/board/write")
    public ResponseEntity<List<Category>> findBoardWriteForm() {
        log.debug("findBoardWriteForm 호출");

        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * 게시물을 작성합니다
     */
    @PostMapping("/board")
    public ResponseEntity insertBoard(@RequestPart(value = "file", required = false) MultipartFile[] multipartFiles,
                                      @RequestParam(value = "categoryIdx") Integer categoryIdx,
                                      @RequestParam(value = "title") String title,
                                      @RequestParam(value = "writer") String writer,
                                      @RequestParam(value = "content") String content,
                                      @RequestParam(value = "password") String password) {
        log.debug("insertBoard 호출");

        // 1-1. Board 도메인 객체 생성
        Board board = Board.builder().categoryIdx(categoryIdx).title(title).writer(writer).content(content).password(password).build();
        // 1-2. File 저장 후 도메인 객체 생성
        List<File> files = FileUtils.toFilesAfterUpload(multipartFiles);

        // 2. 유효성 검증
        try {
            BoardValidationUtils.validateOnCreate(board);
            files.forEach(FileValidationUtils::validateOnCreate);
        } catch (IllegalArgumentException e) {
            // 2-1. 예외 발생 -> 디렉토리 파일 삭제
            log.error("예외 발생 -> error : {}", e.getMessage());

            files.forEach(FileUtils::deleteFileFromServerDirectory);
            return ResponseEntity.badRequest().body(e.getMessage()); // Status Code 400
        }

        // 3. 데이터베이스 삽입
        boardService.insert(board);
        files.stream().forEach(file -> fileService.insert(file.updateBoardIdx(board.getBoardIdx())));

        return ResponseEntity.ok(board.getBoardIdx()); // Status Code 200
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @param boardIdx 게시물 번호
     * @param response 응답 객체
     * @return 게시물 정보
     */
    @GetMapping("/board/modify/{boardIdx}")
    public ResponseEntity<Object> findBoardModifyForm(@PathVariable("boardIdx") Long boardIdx, Map<String, Object> response) {
        log.debug("findBoardModifyForm 호출 -> 게시글 번호 : {}", boardIdx);

        response.put("boards", boardService.findByBoardIdx(boardIdx));
        response.put("files", fileService.findAllByBoardIdx(boardIdx));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 번호에 해당하는 게시글 삭제 정보를 응답합니다
     *
     * @param boardIdx 게시물 번호
     * @return 게시물 정보
     */
    @GetMapping("/board/delete")
    public ResponseEntity<Board> deleteBoardForm(@PathVariable("boardIdx") Long boardIdx) {
        log.debug("findBoardModifyForm 호출 -> 게시글 번호 : {}", boardIdx);
        return ResponseEntity.ok(boardService.findByBoardIdx(boardIdx));
    }

    /**
     * 게시물 번호에 해당하는 게시물 삭제을 삭제합니다
     */
    @DeleteMapping("/board/{boardIdx}")
    public ResponseEntity deleteBoard(@PathVariable("boardIdx") Long boardIdx, @RequestParam(value = "password") String password) {
        log.debug("findBoardModifyForm 호출 -> 게시글 번호 : {}", boardIdx);

        // 1. 게시글 확인
        Board board = boardService.findByBoardIdx(boardIdx);
        if (board == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 글을 찾을 수 없습니다."); // Status Code 404
        }

        // TODO: ENUM으로 분리
        // 2. 패스워드 확인
        if (!board.canDelete(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 올바르지 않습니다."); // Status Code 401
        }

        // 3. 댓글 -> 파일 -> 게시글 삭제
        commentService.deleteAllByBoardIdx(board);
        fileService.deleteAllByBoardIdx(board);
        boardService.delete(board);

        return ResponseEntity.ok().body("게시물이 성공적으로 삭제되었습니다."); // Status Code 200
    }

    /**
     * 파일 번호에 해당하는 파일을 응답합니다
     */
    @GetMapping("/download/{fileIdx}")
    public ResponseEntity serveDownloadFile(@PathVariable("fileIdx") Long fileIdx) {
        log.debug("serveDownloadFile 호출 -> 파일 번호 : {}", fileIdx);

        // 1. 파일 확인
        File file = fileService.findByFileIdx(fileIdx);
        if (file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 파일을 찾을 수 없습니다."); // Status Code 404
        }

        // 2. 파일을 바이트 배열로 변환
        byte[] fileContent = FileUtils.convertByteArray(file.getSavedName());

        // 3. 다운로드 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", FileUtils.generateEncodedName(file));
        headers.setContentLength(fileContent.length);

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    /**
     * 게시물 번호에 해당하는 게시물을 수정합니다
     */
    @PutMapping("/board/{boardIdx}")
    public ResponseEntity updateBoard(@PathVariable("boardIdx") Long boardIdx,
                                      @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles,
                                      @RequestParam(value = "categoryIdx") Integer categoryIdx,
                                      @RequestParam(value = "title") String title,
                                      @RequestParam(value = "writer") String writer,
                                      @RequestParam(value = "content") String content,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "fileIdx", required = false) List<Long> previouslyUploadedIndexes) {
        // 1-1. 게시글 원본 가져오기
        Board board = boardService.findByBoardIdx(boardIdx);

        // 1-2. 게시글 원본 확인
        if (board == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 글을 찾을 수 없습니다."); // Status Code 404
        }

        // 2-1. update Board 도메인 객체 생성
        Board updateBoard = Board.builder().boardIdx(boardIdx).categoryIdx(categoryIdx).title(title).writer(writer).content(content).password(password).build();

        // 2-2. 유효성 검증
        try {
            BoardValidationUtils.validateOnUpdate(updateBoard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Status Code 400
        }

        // 2-3. 패스워드 체크
        if( !board.canUpdate(updateBoard.getPassword()) ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 올바르지 않습니다."); // Status Code 401
        }
        // 3-1. 업데이트된 도메인 객체
        Board updatedBoard = board.update(updateBoard);

        // 3-2. File 저장 후 도메인 객체 생성
        List<File> files = FileUtils.toFilesAfterUpload(multipartFiles);

        // 3-3. 유효성 검증
        try {
            files.forEach(FileValidationUtils::validateOnCreate);
        } catch (IllegalArgumentException e) {
            // 3-4. 예외 발생 -> 디렉토리 파일 삭제
            files.forEach(FileUtils::deleteFileFromServerDirectory);
            return ResponseEntity.badRequest().body(e.getMessage()); // Status Code 400
        }

        // 4-1. 데이터베이스에 저장된 기존 파일 인덱스와 인자로 받은 인덱스를 비교
        List<Long> indexesToDelete = new ArrayList<>(fileService.findAllIndexesByBoardIdx(boardIdx));
        if(previouslyUploadedIndexes != null && !previouslyUploadedIndexes.isEmpty()) {
            indexesToDelete.removeAll(previouslyUploadedIndexes);
        }

        // 5-1. 게시물 수정
        boardService.update(updatedBoard);
        // 5-2. 파일 저장
        files.stream().forEach(file -> fileService.insert(file.updateBoardIdx(board.getBoardIdx())));
        // 5-3. 파일 삭제
        indexesToDelete.stream().forEach(fileIdx -> fileService.delete(fileIdx));


        return ResponseEntity.ok().body("게시물이 성공적으로 수정되었습니다."); // Status Code 200
    }
}
