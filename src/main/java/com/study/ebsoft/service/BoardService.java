package com.study.ebsoft.service;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.mapper.BoardMapper;
import com.study.ebsoft.model.board.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    public BoardMapper boardMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper) {
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

    public BoardDTO selectBoardWithImages(Long boardIdx) {
        return boardMapper.selectBoardWithImages(boardIdx);
    }

    public int increaseHit(Long boardIdx) {
        return boardMapper.increaseHit(boardIdx);
    }

    public void insertBoard(BoardDTO board) {
        boardMapper.insertBoard(board);
    }

    public void insertFile(FileDTO file) {
        boardMapper.insertFile(file);
    }

    public int updateBoard(BoardDTO board) {
        return boardMapper.updateBoard(board);
    }

    public List<Long> selectFileIndexes(Long boardIdx) {
        return boardMapper.selectFileIndexes(boardIdx);
    }

    public String selectSavedFileName(Long fileIdx) {
        return boardMapper.selectSavedFileName(fileIdx);
    }

    public int deleteFile(Long fileIdx) {
        return boardMapper.deleteFile(fileIdx);
    }

    public void insertComment(CommentDTO commentDTO) {
        boardMapper.insertComment(commentDTO);
    }

    public CommentDTO selectComment(Long commentIdx) {
        return boardMapper.selectComment(commentIdx);
    }

    public int deleteComment(Long commentIdx) {
        return boardMapper.deleteComment(commentIdx);
    }

    public List<CategoryDTO> selectAllCategory() {
        return boardMapper.selectAllCategory();
    }
}
