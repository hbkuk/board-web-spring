package com.study.ebsoft.controller;

import com.oreilly.servlet.MultipartRequest;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.model.board.Board;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.BuildUtils;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.SearchConditionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
public class BoardController {

    private BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    @GetMapping("/board")
    public ModelAndView showBoard(@RequestParam(name = "board_idx") Long boardIdx, ModelAndView mav) throws ServletException, IOException, NoSuchElementException {
        mav.addObject("board", boardService.findBoardWithDetails(boardIdx));
        mav.setViewName("board");
        return mav;
    }

    /**
     * 검색조건(searchConditionQueryString)에 맞는 전체 게시물 리스트와 View를 응답합니다.
     */
    @GetMapping("/boards")
    public ModelAndView showBoards(HttpServletRequest req, ModelAndView mav) throws ServletException, IOException {
        mav.addObject("boards", boardService.findAllBoardsWithFileCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap())));
        mav.addObject("categories", boardService.findAllCategorys());
        mav.setViewName("boards");
        return mav;
    }

    /**
     * 게시글 작성에 필요한 정보와 View를 응답합니다
     */
    @GetMapping("/board/write")
    public ModelAndView writeForm(HttpServletRequest req, ModelAndView mav) throws ServletException, IOException {
        mav.addObject("categories", boardService.findAllCategorys());
        mav.setViewName("boardWrite");
        return mav;
    }

    /**
     * 게시물을 작성합니다
     */
    @PostMapping("/board/write")
    public ModelAndView boardWrite(HttpServletRequest req, RedirectAttributes redirectAttributes, ModelAndView mav) throws ServletException, IOException {
        // TODO: 예외 발생 시 파일 삭제...
        MultipartRequest multi = FileUtils.fileUpload(req);

        Board board = null;
        List<File> files = null;
        try {
            board = BuildUtils.buildWriteBoardFromRequest(multi);
            files = BuildUtils.buildFilesFromRequest(multi);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            mav.setViewName("redirect:/board/write");
            return mav;
        }

        BoardDTO boardDTO = boardService.saveBoardWithImages(board,files);

        mav.addObject("board_idx", boardDTO.getBoardIdx());
        mav.setViewName("redirect:/board");
        return mav;
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    @GetMapping("/board/modify")
    public ModelAndView boardModifyForm(@RequestParam(name = "board_idx") Long boardIdx, ModelAndView mav) throws ServletException, IOException, NoSuchElementException {
        mav.addObject("board", boardService.findBoardWithImages(boardIdx));
        mav.setViewName("boardModify");
        return mav;
    }

    /**
     * 게시물 번호에 해당하는 게시물을 수정합니다
     */
    @PostMapping("/board/modify")
    public ModelAndView boardModify(HttpServletRequest req, RedirectAttributes redirectAttributes, ModelAndView mav) throws ServletException, IOException, NoSuchElementException {
        MultipartRequest multi = FileUtils.fileUpload(req);

        // TODO: Error Message에 패스워드는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.가 포함됨. BoardDTO 객체를 사용할 것.
        Board updateBoard = null;
        List<File> newUploadFiles = null;
        try {
            updateBoard = BuildUtils.buildModifyBoardFromRequest(multi);
            newUploadFiles = BuildUtils.buildFilesFromRequest(multi);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            mav.addObject("board_idx", Long.parseLong(multi.getParameter("board_idx")));
            mav.setViewName("redirect:/board/modify");
            return mav;
        }

        List<Long> previouslyUploadedIndexes = new ArrayList<Long>();
        if (multi.getParameter("file_idx") != null) {
            for (String item : multi.getParameterValues("file_idx")) {
                previouslyUploadedIndexes.add(Long.parseLong(item));
            }
        }

        try {
            boardService.updateBoardWithImages(updateBoard, newUploadFiles, previouslyUploadedIndexes);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            mav.addObject("board_idx", Long.parseLong(multi.getParameter("board_idx")));
            mav.setViewName("redirect:/board/modify");
            return mav;
        }
        mav.addObject("board_idx", updateBoard.getBoardIdx().getBoardIdx());
        mav.setViewName("redirect:/board");
        log.debug("redirect URI : {}", mav.getViewName());
        return mav;
    }

    /**
     * 게시글 번호에 해당하는 게시글 수정 정보를 응답합니다
     *
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    @GetMapping("/board/delete")
    public ModelAndView boardDeleteForm(@RequestParam(name = "board_idx") Long boardIdx, ModelAndView mav) throws ServletException, IOException, NoSuchElementException {
        mav.addObject("board", boardService.findBoardWithDetails(boardIdx));
        mav.setViewName("boardDelete");
        return mav;
    }

    /**
     * 게시물 번호에 해당하는 게시물 삭제을 삭제합니다
     */
    @PostMapping("/board/delete")
    public ModelAndView boardDelete(RedirectAttributes redirectAttributes,
                             ModelAndView mav,
                             @ModelAttribute BoardDTO deleteBoard,
                             @RequestParam("board_idx") Long boardIdx,
                             @RequestParam("passwrd") String password) throws ServletException, IOException, NoSuchElementException {

        deleteBoard.setBoardIdx(boardIdx);
        deleteBoard.setPassword(password);

        try {
            boardService.deleteBoardWithFilesAndComment(deleteBoard);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            mav.addObject("board_idx", deleteBoard.getBoardIdx());
            mav.setViewName("redirect:/board");
            log.debug("redirect URI : {}", mav.getViewName());
            return mav;
        }
        mav.setViewName("redirect:/boards");
        log.debug("redirect URI : {}", mav.getViewName());
        return mav;
    }

    /**
     * 파일 번호에 해당하는 파일을 응답합니다
     */
    @GetMapping("/download")
    public void download(@RequestParam(name = "file_idx") Long fileIdx, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FileDTO fileDTO = boardService.findFileById(fileIdx);
        if (fileDTO == null) {
            throw new NoSuchElementException("파일을 찾지 못했습니다.");
        }

        FileUtils.serveDownloadFile(req, resp, fileDTO.getSavedFileName(), fileDTO.getOriginalFileName());
    }
}
