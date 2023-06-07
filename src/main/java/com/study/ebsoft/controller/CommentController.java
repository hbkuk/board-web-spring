package com.study.ebsoft.controller;

import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.service.CommentService;
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
     *
     * @param comment 댓글 정보를 담고 있는 커맨드 객체 (Comment)
     * @return 응답 결과
     */
    @PostMapping("/comment")
    public ResponseEntity insertComment(@RequestBody Comment comment) {
        log.debug("insertComment 호출");

        try {
            commentService.insert(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Status Code 400
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(comment.getCommentIdx()); // Status Code 201
    }

    /**
     * 댓글 번호에 해당하는 댓글을 삭제합니다
     *
     * @param commentIdx 댓글 번호
     * @param password   비밀번호
     * @return 응답 결과
     */
    @DeleteMapping("/comment/{commentIdx}")
    public ResponseEntity deleteComment(@PathVariable("commentIdx") Long commentIdx,
                                        @RequestParam(value = "password") String password) {
        log.debug("deleteComment 호출");

        // 패스워드 체크..
        Comment comment = commentService.findByCommentIdx(commentIdx);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 댓글을 찾을 수 없습니다."); // Status Code 404
        }

        if (!comment.canDelete(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 다릅니다."); // Status Code 401
        }

        commentService.delete(comment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Status Code 201
    }
}

