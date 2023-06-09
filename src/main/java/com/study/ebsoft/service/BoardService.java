package com.study.ebsoft.service;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.dto.Page;
import com.study.ebsoft.dto.SearchCondition;
import com.study.ebsoft.exception.BoardNotFoundException;
import com.study.ebsoft.exception.InvalidPasswordException;
import com.study.ebsoft.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
     *
     * @return 게시물 목록
     */
    public List<Board> findAllBySearchCondition(Map<String, Object> conditionMap) {
        return boardRepository.findAllBySearchCondition(conditionMap);
    }

    /**
     * 게시물 번호를 인자로 받아 해당 게시물을 가져온 후 조회수를 1 증가시킨 후 리턴합니다.
     *
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 Board, 그렇지 않다면 BoardNotFoundException 던집니다.
     */
    public Board findByBoardIdx(Long boardIdx) {
        Board board = boardRepository.findByBoardIdx(boardIdx);
        if (board == null) {
            throw new BoardNotFoundException("해당 글을 찾을 수 없습니다.");
        }
        boardRepository.increaseHit(boardIdx);
        return board;
    }

    /**
     * 게시물을 저장합니다.
     *
     * @param board 게시물 정보가 담긴 객체
     */
    public void insert(Board board) {
        boardRepository.insert(board);
    }

    /**
     * 게시물을 수정합니다.
     *
     * @param updatedBoard 수정된 게시물 정보가 담긴 객체
     */
    public void update(Board updatedBoard) {
        boardRepository.update(updatedBoard);
    }

    /**
     * 게시물을 삭제합니다.
     *
     * @param board 게시물 정보가 담긴 객체
     */
    public void delete(Board board) {
        boardRepository.delete(board);
    }

    public Page createPagination(Map<String, Object> conditionMap) {
        return ((Page) conditionMap.get("page")).calculatePaginationInfo(boardRepository.findBoardCount(conditionMap));
    }
}
