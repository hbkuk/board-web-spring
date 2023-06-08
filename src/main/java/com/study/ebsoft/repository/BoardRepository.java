package com.study.ebsoft.repository;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.SearchConditionDTO;
import com.study.ebsoft.domain.Board;
import com.study.ebsoft.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {

    public BoardMapper boardMapper;

    @Autowired
    public BoardRepository(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public BoardDTO findAllBySearchCondition(SearchConditionDTO searchCondition) {
        return boardMapper.findAllBySearchCondition(searchCondition);
    }

    public Board findByBoardIdx(Long boardIdx) {
        return boardMapper.findByBoardIdx(boardIdx);
    }

    public void increaseHit(Long boardIdx) {
        boardMapper.increaseHit(boardIdx);
    }

    public void insert(Board board) {
        boardMapper.insert(board);
    }

    public void delete(Board board) {
        boardMapper.delete(board);
    }

    public void update(Board board) {
        boardMapper.update(board);
    }
}
