package com.study.ebsoft.repository;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardRepository {

    public BoardMapper boardMapper;

    @Autowired
    public BoardRepository(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public List<BoardDTO> selectBoards() {
        return boardMapper.selectBoards();
    }

    public BoardDTO selectBoard(Long boardIdx) {
        return boardMapper.selectBoard(boardIdx);
    }

    public BoardDTO selectBoardWithDetails(Long boardIdx) {
        return boardMapper.selectBoardWithDetails(boardIdx);
    }

    public BoardDTO selectBoardWithFiles(Long boardIdx) {
        return boardMapper.selectBoardWithFiles(boardIdx);
    }

    public List<Long> selectFileIndexes(Long boardIdx) {
        return boardMapper.selectFileIndexes(boardIdx);
    }

    public String selectSavedFileName(Long fileIdx) {
        return boardMapper.selectSavedFileName(fileIdx);
    }

    public CommentDTO selectComment(Long commentIdx) {
        return boardMapper.selectComment(commentIdx);
    }

    public List<CategoryDTO> selectAllCategory() {
        return boardMapper.selectAllCategory();
    }

    public FileDTO selectFile(Long fileIdx) {
        return boardMapper.selectFile(fileIdx);
    }



    public void insertBoard(BoardDTO board) {
        boardMapper.insertBoard(board);
    }

    public void insertFile(FileDTO file) {
        boardMapper.insertFile(file);
    }

    public void insertComment(CommentDTO commentDTO) {
        boardMapper.insertComment(commentDTO);
    }



    public int increaseHit(Long boardIdx) {
        return boardMapper.increaseHit(boardIdx);
    }

    public int updateBoard(BoardDTO board) {
        return boardMapper.updateBoard(board);
    }



    public int deleteBoard(BoardDTO board) {
        return boardMapper.deleteBoard(board);
    }

    public int deleteComment(CommentDTO comment) {
        return boardMapper.deleteComment(comment);
    }

    public int deleteFile(Long fileIdx) {
        return boardMapper.deleteFile(fileIdx);
    }
    public int deleteAllComment(Long boardIdx) {
        return boardMapper.deleteAllComment(boardIdx);

    }
}
