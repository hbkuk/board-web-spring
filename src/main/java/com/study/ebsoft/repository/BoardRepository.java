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

    public List<BoardDTO> findBoards() {
        return boardMapper.findBoards();
    }

    public BoardDTO findBoard(Long boardIdx) {
        return boardMapper.findBoard(boardIdx);
    }

    public BoardDTO findBoardAndCommentAndFile(Long boardIdx) {
        return boardMapper.findBoardAndCommentAndFile(boardIdx);
    }

    public BoardDTO findBoardFile(Long boardIdx) {
        return boardMapper.findBoardFile(boardIdx);
    }

    public List<Long> findFileIndexes(Long boardIdx) {
        return boardMapper.findFileIndexes(boardIdx);
    }

    public String findSavedFileName(Long fileIdx) {
        return boardMapper.findSavedFileName(fileIdx);
    }

    public CommentDTO findComment(Long commentIdx) {
        return boardMapper.findComment(commentIdx);
    }

    public List<CategoryDTO> findAllCategory() {
        return boardMapper.findAllCategory();
    }

    public FileDTO findFile(Long fileIdx) {
        return boardMapper.findFile(fileIdx);
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
