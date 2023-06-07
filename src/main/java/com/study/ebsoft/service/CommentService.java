package com.study.ebsoft.service;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * 모든 카테고리를 리턴합니다
     *
     * @return 모든 카테고리
     */
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public void deleteAllByBoardIdx(Board board) {
        commentRepository.deleteAllByBoardIdx(board);
    }
}
