package com.study.ebsoft.service;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.repository.CommentRepository;
import com.study.ebsoft.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment findByCommentIdx(Long commentIdx) {
        Comment comment = commentRepository.findByCommentIdx(commentIdx);
        if (comment == null) {
            throw new NoSuchElementException("해당 댓글을 찾을 수 없습니다.");
        }
        return comment;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public void insert(Comment comment) {
        ValidationUtils.validateComment(comment);
        commentRepository.insert(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public void deleteAllByBoardIdx(Board board) {
        commentRepository.deleteAllByBoardIdx(board);
    }
}
