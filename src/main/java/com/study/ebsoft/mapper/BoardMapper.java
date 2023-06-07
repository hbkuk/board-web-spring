package com.study.ebsoft.mapper;

import com.study.ebsoft.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<Board> findAll();

    Board findByBoardIdx(Long boardIdx);

    void increaseHit(Long boardIdx);

    void insert(Board board);

    void delete(Board board);

    void update(Board board);
}
