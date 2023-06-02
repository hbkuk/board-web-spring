package com.study.ebsoft.mapper;

import com.study.ebsoft.dto.BoardDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    BoardDTO showTestBoard(Long boardIdx);
}
