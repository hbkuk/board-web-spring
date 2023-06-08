package com.study.ebsoft.mapper;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.SearchConditionDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    BoardDTO findAllBySearchCondition(SearchConditionDTO searchCondition);

    Board findByBoardIdx(Long boardIdx);

    void increaseHit(Long boardIdx);

    void insert(Board board);

    void delete(Board board);

    void update(Board board);
}
