package com.study.ebsoft.service;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.SearchConditionDTO;
import com.study.ebsoft.domain.Board;
import com.study.ebsoft.exception.InvalidPasswordException;
import com.study.ebsoft.repository.BoardRepository;
import com.study.ebsoft.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
    public BoardDTO findAllBySearchCondition(SearchConditionDTO searchCondition) {
        return boardRepository.findAllBySearchCondition(searchCondition);
    }

    /**
     * 게시물 번호를 인자로 받아 해당 게시물을 가져온 후 조회수를 1 증가시킨 후 리턴합니다.
     *
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 Board, 그렇지 않다면 NoSuchElementException 던집니다.
     */
    public Board findByBoardIdx(Long boardIdx) {
        Board board = boardRepository.findByBoardIdx(boardIdx);
        if( board == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
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
        ValidationUtils.validateBoard(board);
        boardRepository.insert(board);
    }

    /**
     * 게시물을 수정합니다.
     *
     * @param board       원글 정보가 담긴 객체
     * @param updateBoard 수정 정보가 담긴 객체
     */
    public void update(Board board, Board updateBoard) {
        ValidationUtils.validateBoard(updateBoard);

        if( !board.canUpdate(updateBoard.getPassword()) ) {
            throw new InvalidPasswordException("비밀번호가 다릅니다.");
        }

        boardRepository.update(board.update(updateBoard));
    }

    /**
     * 게시물을 삭제합니다.
     *
     * @param board 게시물 정보가 담긴 객체
     */
    public void delete(Board board) {
        boardRepository.delete(board);
    }
}
