package com.study.ebsoft.service;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Category;
import com.study.ebsoft.domain.File;
import com.study.ebsoft.repository.CategoryRepository;
import com.study.ebsoft.repository.FileRepository;
import com.study.ebsoft.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FileService {

    FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * 모든 카테고리를 리턴합니다
     *
     * @return 모든 카테고리
     */
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public void insert(File file) {
        fileRepository.insert(file);
    }

    public List<File> findAllByBoardIdx(Long boardIdx) {
        return fileRepository.findAllByBoardIdx(boardIdx);
    }

    public void deleteAllByBoardIdx(Board board) {

        List<File> files = fileRepository.findAllByBoardIdx(board.getBoardIdx());
        files.forEach(file -> FileUtils.deleteUploadedFile(file.getSavedName()));

        fileRepository.deleteAllByBoardIdx(board);
    }

    public File findByFileIdx(Long fileIdx) {
        return fileRepository.findByFileIdx(fileIdx);
    }

    public List<Long> findAllIndexesByBoardIdx(Long boardIdx) {
        return fileRepository.findAllIndexesByBoardIdx(boardIdx);
    }

    public void delete(Long fileIdx) {
        FileUtils.deleteUploadedFile(findByFileIdx(fileIdx).getSavedName());
        fileRepository.delete(fileIdx);
    }
}
