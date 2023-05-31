package com.study.ebsoft.controller;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.service.BoardService;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.SearchConditionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/board/update/form")
    public ModelAndView boardUpdateForm(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws ServletException, IOException, NoSuchElementException {
        BoardDTO boardDTO = boardService.findBoardWithDetails(Long.parseLong(req.getParameter("board_idx")));

        modelAndView.addObject("board", boardDTO);
        modelAndView.setViewName("boardModifyView");
        return modelAndView;
    }

    /**
     * 파일 번호에 해당하는 파일을 응답합니다
     */
    @GetMapping("/download")
    public String download(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FileDTO fileDTO = boardService.findFileById(Long.parseLong(req.getParameter("file_idx")));
        if( fileDTO == null ) {
            throw new NoSuchElementException("파일을 찾지 못했습니다.");
        }

        FileUtils.serveDownloadFile(req, resp, fileDTO.getSavedFileName(), fileDTO.getOriginalFileName());
        return null;
    }

    /**
     * 게시글 번호에 해당하는 게시글 수정 정보를 응답합니다
     *
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    @GetMapping("/board/modify/form")
    public String boardModifyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NoSuchElementException {
        BoardDTO boardDTO = boardService.findBoardWithImages(Long.parseLong(req.getParameter("board_idx")));
        req.setAttribute("board", boardDTO);
        return "/boardModifyView";
    }

    /**
     * 게시글 번호에 해당하는 게시글 정보를 응답합니다
     *
     * @throws NoSuchElementException 게시글 번호에 해당하는 게시물이 없는 경우 예외를 던집니다
     */
    @GetMapping("/board")
    public String showBoard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NoSuchElementException {
        BoardDTO boardDTO = boardService.findBoardWithDetails((Long.parseLong(req.getParameter("board_idx"))));
        req.setAttribute("board", boardDTO);
        return "/boardView";
    }

    /**
     * 검색조건(searchConditionQueryString)에 맞는 전체 게시물 리스트와 View를 응답합니다.
     */
    @GetMapping("/boards")
    public String showBoards(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(req.getParameterMap()));
        req.setAttribute("boards", boardService.findAllBoardsWithFileCheck(SearchConditionUtils.buildQueryCondition(req.getParameterMap())));
        req.setAttribute("categories", boardService.findAllCategorys());

        return "/views/boards.jsp";
    }

    /**
     * 게시글 작성에 필요한 정보와 View를 응답합니다
     */
    @GetMapping("/board/write/form")
    public String process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", boardService.findAllCategorys());

        return "/boardWriteView";
    }
}
