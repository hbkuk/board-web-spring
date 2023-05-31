package com.study.ebsoft.service;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.model.board.Board;
import com.study.ebsoft.model.comment.Comment;
import com.study.ebsoft.model.file.File;
import com.study.ebsoft.repository.board.BoardDAO;
import com.study.ebsoft.repository.category.CategoryDAO;
import com.study.ebsoft.repository.comment.CommentDAO;
import com.study.ebsoft.repository.file.FileDAO;
import com.study.ebsoft.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoardService {

    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private FileDAO fileDAO;

    /**
     * 검색 조건에 맞는 모든 게시물에 대한 정보와 해당 게시물에 업로드된 파일의 존재여부를 생성해 리턴합니다
     */
    public List<BoardDTO> findAllBoardsWithFileCheck(String searchConditionQuery) {
        return boardDAO.findAllWithFileCheck(searchConditionQuery);
    }

    /**
     * 게시물 번호를 인자로 받아 번호에 해당하는 게시물, 모든 댓글, 모든 파일 정보를 생성해 리턴합니다
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO findBoardWithDetails(long boardIdx) throws NoSuchElementException {
        if( boardDAO.increaseHitCount(boardIdx) == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        boardDTO.setComments(commentDAO.findAllByBoardId(boardIdx));
        boardDTO.setFiles(fileDAO.findFilesByBoardId(boardIdx));

        return boardDTO;
    }

    /**
     * 게시물 번호를 인자로 받아 번호에 해당하는 게시물, 모든 파일 정보를 생성해 리턴합니다
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO findBoardWithImages(long boardIdx) throws NoSuchElementException {
        log.debug("getBoardWithImages() 메서드 호출시 BoardIdx: {}", boardIdx);

        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("getBoardWithImages() -> findById -> BoardIDX : {}", boardDTO.getBoardIdx());
        boardDTO.setFiles(fileDAO.findFilesByBoardId(boardIdx));

        return boardDTO;
    }

    /**
     * 게시물 정보와 업로드 할 파일을 인자로 받아 저장하고 게시물 번호를 리턴합니다
     * @param board 게시물 정보
     * @param files 업로드 할 파일
     * @return 게시물이 저장되었다면 게시물 번호만 담긴 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO saveBoardWithImages(Board board, List<File> files) {
        log.debug(" saveBoardWithImages() 메서드 호출 -> board : {} , files의 size : {} ",
                board.toString(), files.size());
        BoardDTO boardDTO = boardDAO.save(board);

        if (files.size() != 0) {
            files.forEach(file -> fileDAO.save(file, boardDTO.getBoardIdx()));
        }

        return boardDTO;
    }

    /**
     * 수정할 게시물 정보와 추가 또는 삭제할 파일의 정보를 인자로 받아서 수정하고 게시물 번호를 리턴합니다<br>
     * 사용자가 삭제한 경우 {@code previouslyUploadedIndexes}에 파일의 번호가 포함되지 않고 인자로 전달됩니다
     * @param updateBoard 수정할 게시물 정보
     * @param newUploadFiles 추가로 업로드 할 파일 정보
     * @param previouslyUploadedIndexes 이전에 업로드 된 파일의 번호
     * @return 게시물 수정이되었다면 게시물 번호가 담긴 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO updateBoardWithImages(Board updateBoard, List<File> newUploadFiles, List<Long> previouslyUploadedIndexes) {
        log.debug(" updateBoardWithImages() 메서드 호출 -> updateBoard : {} , newUploadFiles size : {}, previouslyUploadedIndexes size : {}",
                updateBoard.toString(), newUploadFiles.size(), previouslyUploadedIndexes.size());

        BoardDTO findBoardDTO = boardDAO.findById(updateBoard.getBoardIdx().getBoardIdx());
        if( findBoardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        BoardDTO updateReturnBoardDTO = boardDAO.update(updateBoard);
        if( updateReturnBoardDTO == null ) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        // DB 확인
        List<Long> dbFileIndexes = fileDAO.findFileIndexesByBoardId(updateReturnBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = new ArrayList<>(dbFileIndexes);
        indexesToDelete.removeAll(previouslyUploadedIndexes);

        // 파일 삭제
        deleteFilesFromdatabaseAndDirectory(indexesToDelete);

        // 새 이미지 추가
        newUploadFiles.forEach(file -> fileDAO.save(file, updateReturnBoardDTO.getBoardIdx()));

        return updateReturnBoardDTO;
    }

    /**
     * 삭제할 파일의 번호를 인자로 받아 데이터베이스 및 디렉토리에서 해당 파일 정보를 삭제합니다
     * @param indexesToDelete 삭제할 파일의 번호 리스트
     */
    private void deleteFilesFromdatabaseAndDirectory(List<Long> indexesToDelete) {

        // 저장 디렉토리에서 파일 삭제
        List<String> fileNamesToDelete = indexesToDelete.stream()
                .map(fileIdx -> fileDAO.findFileNameById(fileIdx).getSavedFileName())
                .collect(Collectors.toList());

        fileNamesToDelete.stream()
                .forEach(fileName -> FileUtils.deleteUploadedFile(fileName));

        // DB 파일 삭제
        indexesToDelete.stream()
                .forEach(fileIdx -> fileDAO.deleteByFileId(fileIdx));

    }

    /**
     * 삭제할 게시물의 정보를 인자로 받아 게시물 번호에 해당하는 게시물, 댓글, 파일 정보를 삭제합니다.
     * @param deleteBoardDTO 삭제할 게시물 정보
     */
    public void deleteBoardWithFilesAndComment(BoardDTO deleteBoardDTO) throws IllegalArgumentException {
        BoardDTO boardDTO = boardDAO.findById(deleteBoardDTO.getBoardIdx());

        if( !boardDTO.getPassword().equals(deleteBoardDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        commentDAO.deleteAllByBoardIdx(deleteBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = fileDAO.findFileIndexesByBoardId(boardDTO.getBoardIdx());
        deleteFilesFromdatabaseAndDirectory(indexesToDelete);

        boardDAO.deleteById(deleteBoardDTO.getBoardIdx(), deleteBoardDTO.getPassword());
    }

    /**
     * 댓글 정보를 인자로 받아 게시물 번호
     * @param comment 댓글 정보
     * @return 댓글이 저장되었다면 게시물 번호만 담긴 CommentDTO, 그렇지 않다면 null
     */
    public CommentDTO saveComment(Comment comment) throws NoSuchElementException {
        BoardDTO boardDTO = boardDAO.findById(comment.getBoardIdx().getBoardIdx());
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("New Comment / request! Comment  : {} ", comment);

        return commentDAO.save(comment);
    }

    //TODO: javadocs
    /**
     *
     * @param deleteComment
     * @return
     */
    public Long deleteCommentByCommentIdx(CommentDTO deleteComment) {
        CommentDTO commentDTO = commentDAO.findByCommentIdx(deleteComment.getCommentIdx());

        if( !commentDTO.getPassword().equals(deleteComment.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
        commentDAO.deleteCommentByCommentIdx(deleteComment);

        return deleteComment.getBoardIdx();
    }

    //TODO: javadocs
    /**
     *
     * @return
     */
    public List<CategoryDTO> findAllCategorys() {
        return categoryDAO.findAll();
    }

    //TODO: javadocs
    /**
     *
     * @param fileIdx
     * @return
     */
    public FileDTO findFileById(long fileIdx) {
        return fileDAO.findFileNameById(fileIdx);
    }


}
