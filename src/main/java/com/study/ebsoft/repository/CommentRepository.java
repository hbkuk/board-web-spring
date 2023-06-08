package com.study.ebsoft.repository;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    public CommentMapper commentMapper;

    @Autowired
    public CommentRepository(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> findAll() {
        return commentMapper.findAll();
    }

    public void deleteAllByBoardIdx(Long boardIdx) {
        commentMapper.deleteAllByBoardIdx(boardIdx);
    }

    public void insert(Comment comment) {
        commentMapper.insert(comment);
    }

    public void delete(Comment comment) {
        commentMapper.delete(comment);
    }

    public Comment findByCommentIdx(Long commentIdx) {
        return commentMapper.findByCommentIdx(commentIdx);
    }
}
