package com.study.ebsoft.service;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.File;
import com.study.ebsoft.repository.BoardRepository;
import com.study.ebsoft.utils.FileUtils;
import com.study.ebsoft.utils.validation.FileValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
public class BoardService {

    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 검색 조건에 맞는 모든 게시물에 대한 정보와 해당 게시물에 업로드된 파일의 존재여부를 생성해 리턴합니다
     */
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    /**
     * 게시물 번호를 인자로 받아
     * 조회수 1을 증가시킨 후 번호에 해당하는 게시물을 생성해 리턴합니다
     *
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 BoardDTO, 그렇지 않다면 null
     */
    public Board findByBoardIdx(Long boardIdx) {
        Board board = boardRepository.findByBoardIdx(boardIdx);
        if( board == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        boardRepository.increaseHit(boardIdx);
        return board;
    }

    public void insert(Board board) {
        boardRepository.insertBoard(board);
    }

    public void delete(Board board) {
        boardRepository.delete(board);
    }

    public void update(Board board) {
        boardRepository.update(board);
    }
}
