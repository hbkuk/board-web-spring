package com.study.ebsoft.mapper;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardDTO> selectBoards();

    BoardDTO selectBoard(Long boardIdx);

    BoardDTO selectBoardWithDetails(Long boardIdx);

    BoardDTO selectBoardWithImages(Long boardIdx);

    CommentDTO selectComment(Long commentIdx);

    List<CategoryDTO> selectAllCategory();

    List<Long> selectFileIndexes(Long boardIdx);

    String selectSavedFileName(Long fileIdx);

    void insertBoard(BoardDTO board);

    void insertFile(FileDTO file);

    void insertComment(CommentDTO commentDTO);

    int updateBoard(BoardDTO boardDTO);

    int increaseHit(Long boardIdx);

    int deleteFile(Long fileIdx);

    int deleteComment(Long commentIdx);
}
