package com.study.ebsoft.repository;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.File;
import com.study.ebsoft.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileRepository {

    public FileMapper fileMapper;

    @Autowired
    public FileRepository(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> findAll() {
        return fileMapper.findAll();
    }

    public void insert(File file) {
        fileMapper.insert(file);
    }

    public List<File> findAllByBoardIdx(Long boardIdx) {
        return fileMapper.findAllByBoardIdx(boardIdx);
    }

    public void deleteAllByBoardIdx(Long boardIdx) {
        fileMapper.deleteAllByBoardIdx(boardIdx);
    }

    public File findByFileIdx(Long fileIdx) {
        return fileMapper.findByFileIdx(fileIdx);
    }

    public List<Long> findAllIndexesByBoardIdx(Long boardIdx) {
        return fileMapper.findAllIndexesByBoardIdx(boardIdx);
    }

    public void delete(Long fileIdx) {
        fileMapper.delete(fileIdx);
    }
}
