package com.study.service;

import com.ebrain.ebsoft.EbsoftApplication;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.model.board.BoardIdx;
import com.study.ebsoft.model.board.Password;
import com.study.ebsoft.model.comment.Comment;
import com.study.ebsoft.model.comment.CommentContent;
import com.study.ebsoft.model.comment.CommentWriter;
import com.study.ebsoft.service.BoardService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EbsoftApplication.class)
public class BoardServiceTest {
    private static Logger log = LoggerFactory.getLogger(BoardServiceTest.class);

    @Autowired
    public BoardService boardService;

    @Test
    public void selectBoards() {
        List<BoardDTO> boards = boardService.selectBoards();

        log.info(boards.toString());
        assertThat(boards).hasSize(10);
    }

    @Test
    public void selectBoard() {
        Long boardIdx = 5L;
        BoardDTO boardDTO = boardService.selectBoard(boardIdx);

        log.info(boardDTO.toString());
        assertThat(boardDTO.getTitle()).isEqualTo("Title 5");
        assertThat(boardDTO.getWriter()).isEqualTo("테스터5");
    }

    @Test
    public void selectBoardWithDetails() {
        Long boardIdx = 1L;
        BoardDTO boardDTO = boardService.selectBoardWithDetails(boardIdx);

        log.info(boardDTO.toString());
        assertThat(boardDTO.getBoardIdx()).isEqualTo(1);
        assertThat(boardDTO.getComments()).hasSize(3);
        assertThat(boardDTO.getFiles()).hasSize(3);
    }

    @Test
    public void selectBoardWithImages() {
        Long boardIdx = 10L;
        BoardDTO boardDTO = boardService.selectBoardWithImages(boardIdx);

        log.info(boardDTO.toString());
        assertThat(boardDTO.getBoardIdx()).isEqualTo(10);
        assertThat(boardDTO.getComments()).isNull();
        assertThat(boardDTO.getFiles()).hasSize(1);
    }

    @Test
    public void increaseHit() {
        Long boardIdx = 1L;
        int affectedRows = boardService.increaseHit(boardIdx);

        assertThat(affectedRows).isEqualTo(1);

        BoardDTO boardDTO = boardService.selectBoardWithDetails(boardIdx);

        assertThat(boardDTO.getHit()).isEqualTo(1);
    }

    @Test
    public void insertBoard() {
        BoardDTO board = BoardDTO.builder()
                .categoryIdx(2)
                .title("제목 1")
                .writer("테스터")
                .content("내용 1")
                .password("rkskekfkakqkt!1")
                .build();

        boardService.insertBoard(board);
        log.debug(board.toString());

        assertThat(board.getBoardIdx()).isEqualTo(11);
    }

    @Test
    public void insertFile() {
        FileDTO file = FileDTO.builder()
                .savedFileName("test.jpg")
                .originalFileName("test.jpg")
                .fileSize(127904)
                .boardIdx(1)
                .build();

        boardService.insertFile(file);
        log.debug(file.toString());

        assertThat(file.getFileIdx()).isEqualTo(13);
        assertThat(file.getSavedFileName()).isEqualTo("test.jpg");
    }

    @Test
    public void updateBoard() {
        BoardDTO board = BoardDTO.builder()
                .boardIdx(1)
                .categoryIdx(5)
                .title("제목 999로 수정")
                .writer("테스터 999")
                .content("내용 999")
                .password("password1!")
                .build();

        int affectedRows = boardService.updateBoard(board);
        assertThat(affectedRows).isEqualTo(1);

        BoardDTO selectBoard = boardService.selectBoard(1L);
        assertThat(selectBoard.getTitle()).isEqualTo("제목 999로 수정");
    }

    @Test
    public void selectFileIndexes() {
        List<Long> fileIndexes = boardService.selectFileIndexes(1L);
        assertThat(fileIndexes).hasSize(3);
        assertThat(fileIndexes).contains(1L).contains(2L).contains(3L);
    }

    @Test
    public void selectSavedFileName() {
        String fileName = boardService.selectSavedFileName(1L);
        assertThat(fileName).isEqualTo("file1.png");
    }

    @Test
    public void deleteFile() {
        int affectedRows = boardService.deleteFile(1L);
        assertThat(affectedRows).isEqualTo(1);
        assertThat(boardService.selectSavedFileName(1L)).isNull();
    }

    @Test
    public void insertComment() {
        CommentDTO comment = CommentDTO.builder()
                .writer("테스터")
                .password("rkskekfka1!")
                .content("내용 1")
                .boardIdx(1)
                .build();

        boardService.insertComment(comment);

        assertThat(comment.getCommentIdx()).isEqualTo(13);
    }

    @Test
    public void selectComment() {
        CommentDTO comment = boardService.selectComment(1L);

        assertThat(comment.getContent()).isEqualTo("Comment 1");
    }

    @Test
    public void deleteComment() {
        int affectedRows = boardService.deleteComment(1L);

        assertThat(affectedRows).isEqualTo(1);
        assertThat(boardService.deleteComment(1L)).isZero();
    }

    @Test
    public void selectAllCategory() {
        List<CategoryDTO> categories = boardService.selectAllCategory();

        assertThat(categories).hasSize(10);
    }
}