package com.study.ebsoft.service;

import com.study.ebsoft.domain.Comment;
import com.study.ebsoft.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 댓글 번호를 인자로 받아 해당 댓글을 가져온 후 리턴합니다.
     * 
     * @param commentIdx 댓글 번호
     * @return 댓글 번호에 해당하는 댓글이 있다면 Comment, 그렇지 않다면 NoSuchElementException 던집니다.
     */
    public Comment findByCommentIdx(Long commentIdx) {
        Comment comment = commentRepository.findByCommentIdx(commentIdx);
        if (comment == null) {
            throw new NoSuchElementException("해당 댓글을 찾을 수 없습니다.");
        }
        return comment;
    }

    /**
     * 모든 댓글 정보를 리턴합니다.
     *
     * @return 댓글 목록
     */
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    /**
     * 댓글을 저장합니다.
     * 
     * @param comment 댓글 정보가 담긴 객체
     */
    public void insert(Comment comment) {
        commentRepository.insert(comment);
    }

    /**
     * 댓글을 삭제합니다.
     * 
     * @param comment 댓글 정보가 담긴 객체
     */
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    /**
     * 게시글 번호를 인자로 받아 해당하는 모든 댓글을 삭제합니다
     *
     * @param boardIdx 게시물 번호
     */
    public void deleteAllByBoardIdx(Long boardIdx) {
        commentRepository.deleteAllByBoardIdx(boardIdx);
    }

    public List<Comment> findAllByBoardIdx(Long boardIdx) {
        return commentRepository.findAllByBoardIdx(boardIdx);
    }
}
