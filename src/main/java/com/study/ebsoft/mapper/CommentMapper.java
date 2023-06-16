package com.study.ebsoft.mapper;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> findAll();

    void deleteAllByBoardIdx(Long boardIdx);

    void insert(Comment comment);

    void delete(Comment comment);

    Comment findByCommentIdx(Long commentIdx);

    List<Comment> findAllByBoardIdx(Long boardIdx);
}
