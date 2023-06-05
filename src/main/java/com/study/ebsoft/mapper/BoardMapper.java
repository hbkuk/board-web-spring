package com.study.ebsoft.mapper;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardDTO> findBoards();

    BoardDTO findBoard(Long boardIdx);

    BoardDTO findBoardAndCommentAndFile(Long boardIdx);

    BoardDTO findBoardFile(Long boardIdx);

    CommentDTO findComment(Long commentIdx);

    List<CategoryDTO> findAllCategory();

    List<Long> findFileIndexes(Long boardIdx);

    String findSavedFileName(Long fileIdx);

    FileDTO findFile(Long fileIdx);



    void insertBoard(BoardDTO board);

    void insertFile(FileDTO file);

    void insertComment(CommentDTO commentDTO);



    int updateBoard(BoardDTO boardDTO);

    int increaseHit(Long boardIdx);



    int deleteBoard(BoardDTO board);

    int deleteComment(CommentDTO comment);

    int deleteFile(Long fileIdx);

    int deleteAllComment(Long boardIdx);
}
