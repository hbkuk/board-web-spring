package com.study.ebsoft.mapper;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.dto.Page;
import com.study.ebsoft.dto.SearchCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    List<Board> findAllBySearchCondition(Map<String, Object> conditionMap);

    Board findByBoardIdx(Long boardIdx);

    void increaseHit(Long boardIdx);

    void insert(Board board);

    void delete(Board board);

    void update(Board board);

    int findBoardCount(Map<String, Object> conditionMap);
}
