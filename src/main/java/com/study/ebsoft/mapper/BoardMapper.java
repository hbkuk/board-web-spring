package com.study.ebsoft.mapper;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.SearchConditionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardDTO> findAllBySearchCondition(SearchConditionDTO searchCondition);

    Board findByBoardIdx(Long boardIdx);

    void increaseHit(Long boardIdx);

    void insert(Board board);

    void delete(Board board);

    void update(Board board);
}
