package com.study.ebsoft.mapper;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    List<File> findAll();

    void insert(File file);

    List<File> findAllByBoardIdx(Long boardIdx);

    void deleteAllByBoardIdx(Board board);

    File findByFileIdx(Long fileIdx);

    List<Long> findAllIndexesByBoardIdx(Long boardIdx);

    void delete(Long fileIdx);
}
