package com.study.ebsoft.service;

import com.study.ebsoft.domain.File;
import com.study.ebsoft.repository.FileRepository;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * 모든 파일을 리턴합니다.
     *
     * @return 파일 목록
     */
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    /**
     * 파일을 저장합니다.
     *
     * @param file 파일 정보가 담긴 객체
     */
    public void insert(File file) {
        ValidationUtils.validateFileOnCreate(file);
        fileRepository.insert(file);
    }

    /**
     * 파일 목록과 게시물 번호를 인자로 받아 파일을 저장합니다.
     *
     * @param files    파일 정보가 담긴 파일 리스트
     * @param boardIdx 게시물 번호
     */
    public void insert(List<File> files, Long boardIdx) {
        for (File file : files) {
            insert(file.updateBoardIdx(boardIdx));
        }
    }

    /**
     * 게시물 번호를 인자로 받아 파일 목록을 리턴합니다.
     *
     * @param boardIdx 게시글 번호
     * @return 파일 목록
     */
    public List<File> findAllByBoardIdx(Long boardIdx) {
        return fileRepository.findAllByBoardIdx(boardIdx);
    }

    /**
     * 게시물 번호를 인자로 받아 모든 파일을 삭제합니다.
     *
     * @param boardIdx 게시물 번호
     */
    public void deleteAllByBoardIdx(Long boardIdx) {
        List<File> files = fileRepository.findAllByBoardIdx(boardIdx);

        deleteFilesFromServerDirectory(files);
        fileRepository.deleteAllByBoardIdx(boardIdx);
    }

    /**
     * 파일 번호를 인자로 받아 해당 파일을 리턴합니다.
     *
     * @param fileIdx 파일 번호
     * @return 파일 번호에 해당하는 파일이 있다면 File, 그렇지 않다면 NoSuchElementException 던집니다.
     */
    public File findByFileIdx(Long fileIdx) {
        File file = fileRepository.findByFileIdx(fileIdx);
        if (file == null) {
            throw new NoSuchElementException("해당 파일을 찾을 수 없습니다.");
        }
        return file;
    }

    /**
     * 게시물 번호를 인자로 받아 해당하는 파일 번호 목록을 리턴합니다.
     *
     * @param boardIdx 게시물 번호
     * @return 파일 번호 목록
     */
    public List<Long> findAllIndexesByBoardIdx(Long boardIdx) {
        return fileRepository.findAllIndexesByBoardIdx(boardIdx);
    }

    /**
     * 파일 번호를 인자로 받아 해당하는 파일을 삭제합니다.
     *
     * @param fileIdx 파일 인덱스
     */
    public void delete(Long fileIdx) {
        FileUtils.deleteUploadedFile(findByFileIdx(fileIdx).getSavedName());
        fileRepository.delete(fileIdx);
    }

    /**
     * 파일 번호 목록을 인자로 받아 해당하는 파일들을 삭제합니다.
     *
     * @param fileIndexes 파일 번호 목록
     */
    public void delete(List<Long> fileIndexes) {
        for (Long fileIdx : fileIndexes) {
            delete(fileIdx);
        }
    }

    /**
     * 새로운 파일들과 이전에 업로드된 파일 인덱스 목록을 기준으로 파일을 저장 또는 삭제합니다.
     *
     * @param newFiles                  새롭게 업로드된 파일 목록
     * @param previouslyUploadedIndexes 이전에 업로드된 파일 번호 목록
     * @param boardIdx
     */
    public void update(List<File> newFiles, List<Long> previouslyUploadedIndexes, Long boardIdx) {

        ValidationUtils.validateFileOnCreate(newFiles);

        // 데이터베이스에 저장된 기존 파일 인덱스와 인자로 받은 인덱스를 비교
        List<Long> indexesToDelete = new ArrayList<>(findAllIndexesByBoardIdx(newFiles.get(0).getBoardIdx()));
        if (isNotNull(previouslyUploadedIndexes)) {
            indexesToDelete.removeAll(previouslyUploadedIndexes);
        }

        insert(newFiles, boardIdx);
        delete(indexesToDelete);
    }

    /**
     * 이전에 업로드된 파일 번호 목록을 인자로 받아 비어있다면 true, 그렇지 않다면 false를 리턴합니다.
     *
     * @param previouslyUploadedIndexes 이전에 업로드된 파일 번호 목록
     * @return 목록이 비어있다면 true, 그렇지 않다면 false를 리턴
     */
    private boolean isNotNull(List<Long> previouslyUploadedIndexes) {
        return previouslyUploadedIndexes != null && !previouslyUploadedIndexes.isEmpty();
    }

    public void deleteFilesFromServerDirectory(List<File> files) {
        for (File file : files) {
            FileUtils.deleteUploadedFile(file.getSavedName());
        }
        files.clear(); // 파일 삭제 후 리스트 비우기
    }

    public List<File> processUploadedFiles(MultipartFile[] multipartFiles) {
        List<File> files = new ArrayList<>();

        if (hasExistUploadFile(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = processUploadedFile(multipartFile);
                if( file == null) {
                    // 예외 발생 시 모든 파일 삭제
                    deleteFilesFromServerDirectory(files);
                    break;
                }
                files.add(file);
            }
        }
        return files;
    }

    private File processUploadedFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String fileName = multipartFile.getOriginalFilename();
        String systemName = FileUtils.generateSystemName(fileName);
        int fileSize = (int) multipartFile.getSize();

        try {
            multipartFile.transferTo(FileUtils.createAbsolutePath(systemName));
            log.debug("업로드 완료 .. 저장된 파일 이름 : {} ", systemName);
        } catch (IOException e) {
            // 파일 업로드 중 에러 발생 시 파일 삭제
            log.error("파일 업로드 중 예외 발생 -> error : {}", e.getMessage());
            return null;
        }

        return File.builder()
                .savedName(systemName)
                .originalName(fileName)
                .fileSize(fileSize)
                .build();
    }

    private static boolean hasExistUploadFile(MultipartFile[] multipartFiles) {
        return multipartFiles != null && multipartFiles.length > 0;
    }
}