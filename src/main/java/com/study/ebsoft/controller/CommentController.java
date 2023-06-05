package com.study.ebsoft.controller;

import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import java.io.IOException;

@Slf4j
@Controller
public class CommentController {
    private BoardService boardService;

    @Autowired
    public CommentController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 댓글을 작성합니다
     */
    @PostMapping("/comment/write")
    public ModelAndView writeComment(RedirectAttributes redirectAttributes, ModelAndView mav,
                                     @RequestParam("comment_writer") String writer,
                                     @RequestParam("comment_password") String password,
                                     @RequestParam("comment_content") String content,
                                     @RequestParam("board_idx") Long boardIdx) {

        CommentDTO comment = null;
        try {
            // TODO: build 클래스 분리 
            comment = CommentDTO.builder()
                    .writer(writer)
                    .password(password)
                    .content(content)
                    .boardIdx(boardIdx)
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            mav.addObject("board_idx", boardIdx);
            mav.setViewName("redirect:/board");
            return mav;
        }
        boardService.insertComment(comment);

        mav.addObject("board_idx", comment.getBoardIdx());
        mav.setViewName("redirect:/board");
        return mav;
    }

    /**
     * 댓글 번호에 해당하는 댓글을 삭제합니다
     */
    @PostMapping("/comment/delete")
    public ModelAndView deleteComment(RedirectAttributes redirectAttributes, ModelAndView mav,
                                      @ModelAttribute CommentDTO deleteComment,
                                      @RequestParam("comment_idx") Long commentIdx,
                                      @RequestParam("board_idx") Long boardIdx,
                                      @RequestParam("password") String password) {

        deleteComment.setCommentIdx(commentIdx);
        deleteComment.setBoardIdx(boardIdx);
        deleteComment.setPassword(password);

        try {
            boardService.deleteCommentByCommentIdx(deleteComment);
        } catch (IllegalArgumentException e) {
            log.error("error : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        mav.addObject("board_idx", deleteComment.getBoardIdx());
        mav.setViewName("redirect:/board");
        log.debug("redirect URI : {}", mav.getViewName());
        return mav;
    }
}

