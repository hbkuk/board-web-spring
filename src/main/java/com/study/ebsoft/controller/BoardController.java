package com.study.ebsoft.controller;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Category;
import com.study.ebsoft.domain.File;
import com.study.ebsoft.dto.Page;
import com.study.ebsoft.dto.SearchCondition;
import com.study.ebsoft.exception.InvalidPasswordException;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.service.CategoryService;
import com.study.ebsoft.service.CommentService;
import com.study.ebsoft.service.FileService;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.validation.BoardValidationGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
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

    /**
     * 검색조건(searchCondition), 페이지 번호에 맞는 전체 게시물을 응답
     *
     * @param searchCondition 검색 조건을 나타내는 {@link SearchCondition} 객체
     * @param page 페이지 정보를 나타내는 {@link Page} 객체
     * @return 검색된 게시글과 페이징 정보를 담은 Map 객체
     */
    @GetMapping("/api/boards")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> findBoards(@Valid @ModelAttribute("searchCondition") SearchCondition searchCondition,
                                          @Valid @ModelAttribute("page") Page page) {
        log.debug("findBoards 호출");
        log.debug("searchCondition : {}, page : {}", searchCondition, page);
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("searchCondition", searchCondition);
        conditionMap.put("page", page);

        response.put("boards", boardService.findAllBySearchCondition(conditionMap));
        response.put("pagination", boardService.createPagination(conditionMap));
        return response;
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @param boardIdx 게시물 번호
     * @param response 응답 맵 객체
     * @return 응답 결과
     */
    @GetMapping("/api/board/{boardIdx}")
    public ResponseEntity<Object> findBoard(@PathVariable("boardIdx") Long boardIdx, Map<String, Object> response) {
        log.debug("findBoard 호출 -> 게시글 번호 : {}", boardIdx);

        response.put("board", boardService.findByBoardIdx(boardIdx));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 작성에 필요한 정보 응답합니다
     *
     * @return 응답 결과
     */
    @GetMapping("/api/board/write")
    public ResponseEntity<List<Category>> findBoardWriteForm() {
        log.debug("findBoardWriteForm 호출");

        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @param boardIdx 게시물 번호
     * @param response 응답 객체
     * @return 응답 결과
     */
    @GetMapping("/api/board/modify/{boardIdx}")
    public ResponseEntity<Object> findBoardModifyForm(@PathVariable("boardIdx") Long boardIdx, Map<String, Object> response) {
        log.debug("findBoardModifyForm 호출 -> 게시글 번호 : {}", boardIdx);

        response.put("board", boardService.findByBoardIdx(boardIdx));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @param boardIdx 게시물 번호
     * @param response 응답 객체
     * @return 응답 결과
     */
    @GetMapping("/board/delete/{boardIdx}")
    public ResponseEntity<Object> findBoardDeleteForm(@PathVariable("boardIdx") Long boardIdx, Map<String, Object> response) {
        log.debug("findBoardModifyForm 호출 -> 게시글 번호 : {}", boardIdx);

        response.put("board", boardService.findByBoardIdx(boardIdx));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 파일 번호에 해당하는 파일을 응답한다.
     *
     * @param fileIdx 파일 번호
     * @return 응답 결과
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
        headers.setContentDispositionFormData("attachment", FileUtils.generateEncodedName(file.getOriginalName()));
        headers.setContentLength(fileContent.length);

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    /**
     * 게시물을 작성합니다
     *
     * @param board          게시글 정보
     * @param multipartFiles 업로드된 파일 배열
     * @return 응답 결과
     */
    @PostMapping(value="/api/board", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> insertBoard(@Validated(BoardValidationGroup.write.class) @RequestPart(value = "board") Board board,
                                         @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) {
        log.debug("insertBoard 호출,  New Board : {}, Multipart : {}", board, multipartFiles);

        boardService.insert(board);
        fileService.insert(multipartFiles, board.getBoardIdx());

        return ResponseEntity.status(HttpStatus.CREATED).body(board); // Status Code 201
    }

    /**
     * 게시물 번호에 해당하는 게시물을 수정합니다
     *
     * @param boardIdx                  게시글 번호
     * @param multipartFiles            업로드된 파일 배열
     * @param updateBoard               업로드 할 게시글 정보
     * @param previouslyUploadedIndexes 기존 업로드된 파일 인덱스 리스트
     * @return 응답 결과
     */
    @PutMapping("/api/board/{boardIdx}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardIdx") Long boardIdx,
                                         @Validated(BoardValidationGroup.update.class) @RequestPart(value = "board") Board updateBoard,
                                         @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles,
                                         @RequestParam(value = "fileIdx", required = false) List<Long> previouslyUploadedIndexes) {
        log.debug("updateBoard 호출,  Update Board : {}, Multipart Files Size: {}, previouslyUploadedIndexes size : {}", updateBoard, (multipartFiles != null ? multipartFiles.length : "null"), previouslyUploadedIndexes.size());

        // 1. 게시글 원본 확인
        Board board = boardService.findByBoardIdx(boardIdx);

        // 2. 패스워드 확인
        if (!board.canUpdate(updateBoard.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 다릅니다.");
        }

        // 3. 객체 update
        // TODO: 수정
        Board updatedBoard = board.update(updateBoard);

        // 4. 게시물 -> 파일 수정
        boardService.update(updatedBoard);
        fileService.update(multipartFiles, previouslyUploadedIndexes, boardIdx);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedBoard); // Status Code 201
    }

    /**
     * 게시물 번호에 해당하는 게시물 삭제을 삭제합니다
     *
     * @param boardIdx 게시글 번호
     * @param password 비밀번호
     * @return 응답 결과
     */
    @DeleteMapping("/board/{boardIdx}")
    public ResponseEntity deleteBoard(@PathVariable("boardIdx") Long boardIdx,
                                      @RequestParam(value = "password") String password) {
        log.debug("deleteBoard 호출 -> 게시글 번호 : {}", boardIdx);

        // 1. 게시글 원본 확인(예외처리는 GlobalExceptionHandler 위임)
        Board board = boardService.findByBoardIdx(boardIdx);

        // 2. 패스워드 확인
        if (!board.canDelete(password)) {
            throw new InvalidPasswordException("비밀번호가 다릅니다.");
        }

        // 3. 댓글 -> 파일 -> 게시물 삭제
        commentService.deleteAllByBoardIdx(board.getBoardIdx());
        fileService.deleteAllByBoardIdx(board.getBoardIdx());
        boardService.delete(board);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Status Code 201
    }
}
