package com.study.ebsoft.service;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.repository.BoardRepository;
import com.study.ebsoft.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoardService {

    BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 검색 조건에 맞는 모든 게시물에 대한 정보와 해당 게시물에 업로드된 파일의 존재여부를 생성해 리턴합니다
     *
     */
    public List<BoardDTO> selectBoards() {
        return boardRepository.selectBoards();
    }

    /**
     * 게시물 번호를 인자로 받아
     * 조회수를 증가시킨 후 번호에 해당하는 게시물, 모든 댓글, 모든 파일 정보를 생성해 리턴합니다
     *
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 BoardDTO, 그렇지 않다면 null
     */
    @Transactional
    public BoardDTO selectBoardWithDetails(Long boardIdx) throws NoSuchElementException {
        log.debug("selectBoardWithDetails() 메서드 호출시 BoardIdx: {}", boardIdx);

        if( boardRepository.increaseHit(boardIdx) == 0 ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        return boardRepository.selectBoardWithDetails(boardIdx);
    }

    /**
     * 게시물 번호를 인자로 받아
     * 번호에 해당하는 게시물, 모든 파일 정보를 생성해 리턴합니다
     *
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO selectBoardWithFiles(Long boardIdx) throws NoSuchElementException {
        log.debug("selectBoardWithFiles() 메서드 호출시 BoardIdx: {}", boardIdx);

        BoardDTO boardDTO = boardRepository.selectBoardWithFiles(boardIdx);

        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        return boardDTO;
    }

    /**
     * 게시물 정보와 업로드 할 파일을 인자로 받아
     * 모두 저장하고 게시물 번호가 담긴 객체를 리턴합니다
     *
     * @param board 게시물과 파일 정보
     * @return 게시물이 저장되었다면 게시물 번호만 담긴 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO insertBoardWithFiles(BoardDTO board) {
        log.debug(" insertBoardWithFiles() 호출 -> board : {} , files의 size : {} ", board.toString(), board.getFiles().size());
        boardRepository.insertBoard(board);

        if (board.getFiles().size() != 0) {
            board.getFiles().forEach(file -> boardRepository.insertFile(file));
        }

        return board;
    }

    /**
     * 수정할 게시물 정보와 추가 또는 삭제할 파일의 정보를 인자로 받아서 수정하고 게시물 번호를 리턴합니다.
     * 사용자가 삭제한 경우 {@code previouslyUploadedIndexes}에 파일의 번호가 포함되지 않고 인자로 전달됩니다
     *
     * @param updateBoard 수정할 게시물 정보와 파이 ㄹ정보
     * @param previouslyUploadedIndexes 이전에 업로드 된 파일의 번호
     * @return 게시물 수정이되었다면 게시물 번호가 담긴 Board, 그렇지 않다면 null
     */
    @Transactional
    public BoardDTO updateBoardWithFiles(BoardDTO updateBoard, List<Long> previouslyUploadedIndexes) {
        log.debug(" updateBoardWithImages() 메서드 호출 -> updateBoard : {} , newUploadFiles size : {}, previouslyUploadedIndexes size : {}",
                updateBoard.toString(), updateBoard.getFiles().size(), previouslyUploadedIndexes.size());

        BoardDTO findBoardDTO = boardRepository.selectBoard(updateBoard.getBoardIdx());
        if( findBoardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        if( boardRepository.updateBoard(updateBoard) == 0 ) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        List<Long> dbFileIndexes = boardRepository.selectFileIndexes(updateBoard.getBoardIdx());

        List<Long> indexesToDelete = new ArrayList<>(dbFileIndexes);
        indexesToDelete.removeAll(previouslyUploadedIndexes);

        List<String> fileNamesToDelete = indexesToDelete.stream()
                .map(fileIdx -> boardRepository.selectSavedFileName(fileIdx))
                .collect(Collectors.toList());

        fileNamesToDelete.stream()
                .forEach(fileName -> FileUtils.deleteUploadedFile(fileName));

        indexesToDelete.stream()
                .forEach(fileIdx -> boardRepository.deleteFile(fileIdx));

        updateBoard.getFiles().forEach(file -> boardRepository.insertFile(file));

        return updateBoard;
    }

    /**
     * 삭제할 게시물의 정보를 인자로 받아
     * 게시물 번호에 해당하는 게시물, 댓글, 파일 정보를 삭제합니다.
     *
     * @param deleteBoardDTO 삭제할 게시물 정보
     */
    @Transactional
    public void deleteBoardWithFilesAndComment(BoardDTO deleteBoardDTO) throws IllegalArgumentException {
        BoardDTO boardDTO = boardRepository.selectBoard(deleteBoardDTO.getBoardIdx());

        if( !boardDTO.getPassword().equals(deleteBoardDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        boardRepository.deleteAllComment(deleteBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = boardRepository.selectFileIndexes(boardDTO.getBoardIdx());

        List<String> fileNamesToDelete = indexesToDelete.stream()
                .map(fileIdx -> boardRepository.selectSavedFileName(fileIdx))
                .collect(Collectors.toList());

        fileNamesToDelete.stream()
                .forEach(fileName -> FileUtils.deleteUploadedFile(fileName));

        indexesToDelete.stream()
                .forEach(fileIdx -> boardRepository.deleteFile(fileIdx));

        boardRepository.deleteBoard(deleteBoardDTO);
    }

    /**
     * 댓글 정보를 인자로 받아 저장하고 게시물 번호가 담긴 Comment 객체를 리턴합니다
     * 게시물 번호를 찾지 못했다면 NoSuchElement 예외를 던집니다
     *
     * @param comment 댓글 정보
     * @return 댓글이 저장되었다면 게시물 번호만 담긴 Comment, 그렇지 않다면 null
     */
    public CommentDTO saveComment(CommentDTO comment) throws NoSuchElementException {
        log.debug("New Comment / request! Comment  : {} ", comment);
        if( boardRepository.selectBoard(comment.getBoardIdx()) == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        boardRepository.insertComment(comment);
        return comment;
    }

    /**
     * 삭제할 댓글 정보를 인자로 받아서 댓글을 삭제하고 게시글 번호를 리턴합니다
     * 패스워드가 같지 않다면 IllegalArgument 예외를 던집니다
     *
     * @param deleteComment 삭제할 댓글 정보
     * @return 게시물 번호
     */
    public Long deleteCommentByCommentIdx(CommentDTO deleteComment) {
        CommentDTO commentDTO = boardRepository.selectComment(deleteComment.getCommentIdx());

        if( !commentDTO.getPassword().equals(deleteComment.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
        boardRepository.deleteComment(deleteComment);

        return deleteComment.getBoardIdx();
    }

    /**
     * 모든 카테고리를 리턴합니다
     *
     * @return 모든 카테고리
     */
    public List<CategoryDTO> selectAllCategory() {
        return boardRepository.selectAllCategory();
    }

    /**
     * 파일 번호를 인자로 받아 번호에 해당하는 File 객체를 생성해 리턴합니다
     *
     * @param fileIdx 파일 번호
     * @return 파일 번호에 해당하는 FileDTO, 찾지 못헀다면 null
     */
    public FileDTO findFileById(Long fileIdx) {
        return boardRepository.selectFile(fileIdx);
    }
}
