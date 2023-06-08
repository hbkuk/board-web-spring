package com.study.ebsoft.service;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.File;
import com.study.ebsoft.repository.FileRepository;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        ValidationUtils.validateFileOnCreate(file);
        fileRepository.insert(file);
    }

    public void insert(List<File> files, Long boardIdx) {
        for (File file : files) {
            insert(file.updateBoardIdx(boardIdx));
        }
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
        File file = fileRepository.findByFileIdx(fileIdx);
        if( file == null ) {
            throw new NoSuchElementException("해당 파일을 찾을 수 없습니다.");
        }
        return file;
    }

    public List<Long> findAllIndexesByBoardIdx(Long boardIdx) {
        return fileRepository.findAllIndexesByBoardIdx(boardIdx);
    }

    public void delete(Long fileIdx) {
        FileUtils.deleteUploadedFile(findByFileIdx(fileIdx).getSavedName());
        fileRepository.delete(fileIdx);
    }

    public void delete(List<Long> fileIndexes) {
        for (Long fileIdx : fileIndexes) {
            delete(fileIdx);
        }
    }

    public void update(List<File> newFiles, List<Long> previouslyUploadedIndexes) {

        ValidationUtils.validateFileOnCreate(newFiles);

        // 데이터베이스에 저장된 기존 파일 인덱스와 인자로 받은 인덱스를 비교
        List<Long> indexesToDelete = new ArrayList<>(findAllIndexesByBoardIdx(newFiles.get(0).getBoardIdx()));
        if (isNotNull(previouslyUploadedIndexes)) {
            indexesToDelete.removeAll(previouslyUploadedIndexes);
        }

        insert(newFiles, newFiles.get(0).getBoardIdx());
        delete(indexesToDelete);
    }

    private boolean isNotNull(List<Long> previouslyUploadedIndexes) {
        return previouslyUploadedIndexes != null && !previouslyUploadedIndexes.isEmpty();
    }
}
