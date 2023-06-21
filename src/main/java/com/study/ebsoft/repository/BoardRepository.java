package com.study.ebsoft.repository;

import com.study.ebsoft.dto.Page;
import com.study.ebsoft.dto.SearchCondition;
import com.study.ebsoft.domain.Board;
import com.study.ebsoft.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BoardRepository {

    public BoardMapper boardMapper;

    @Autowired
    public BoardRepository(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public List<Board> findAllBySearchCondition(Map<String, Object> conditionMap) {
        return boardMapper.findAllBySearchCondition(conditionMap);
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

    public int findBoardCount(Map<String, Object> conditionMap) {
        return boardMapper.findBoardCount(conditionMap);
    }
}
