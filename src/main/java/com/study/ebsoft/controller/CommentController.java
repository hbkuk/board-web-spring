package com.study.ebsoft.controller;

import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.service.CommentService;
import com.study.ebsoft.utils.validation.CommentValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글을 작성합니다
     */
    @PostMapping("/comment")
    public ResponseEntity insertComment(@RequestBody Comment comment) {
        log.debug("insertComment 호출");

        try {
            CommentValidationUtils.validateOnCreate(comment);
        } catch (IllegalArgumentException e) {
            log.error("예외 발생 -> error : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage()); // Status Code 400
        }

        commentService.insert(comment);

        return ResponseEntity.ok(comment.getBoardIdx()); // Status Code 200
    }

    /**
     * 댓글 번호에 해당하는 댓글을 삭제합니다
     */
    @DeleteMapping("/comment/{commentIdx}")
    public ResponseEntity deleteComment(@PathVariable("commentIdx") Long commentIdx, @RequestParam(value = "password") String password) {
        log.debug("deleteComment 호출");

        // 패스워드 체크..
        Comment comment = commentService.findByCommentIdx(commentIdx);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 댓글을 찾을 수 없습니다."); // Status Code 404
        }

        if(!comment.canDelete(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 올바르지 않습니다."); // Status Code 401
        }

        commentService.delete(comment);
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다."); // Status Code 200
    }
}

