package com.study.ebsoft.controller;

import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.SearchConditionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

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
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    @GetMapping("/board/modify")
    public ModelAndView boardUpdateForm(@RequestParam(name = "board_idx") Long boardIdx, ModelAndView mav) throws ServletException, IOException, NoSuchElementException {
        mav.addObject("board", boardService.findBoardWithImages(boardIdx));
        mav.setViewName("boardModify");
        return mav;
    }

    /**
     * 게시글 번호에 해당하는 게시글 수정 정보를 응답합니다
     *
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    @GetMapping("/board/delete")
    public ModelAndView boardModifyForm(@RequestParam(name = "board_idx") Long boardIdx, ModelAndView mav) throws ServletException, IOException, NoSuchElementException {
        mav.addObject("board", boardService.findBoardWithDetails(boardIdx));
        mav.setViewName("boardDelete");
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
