package com.study.repository;

import com.ebrain.ebsoft.EbsoftApplication;
import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.CategoryDTO;
import com.study.ebsoft.dto.CommentDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.repository.BoardRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EbsoftApplication.class)
public class BoardRepositoryTest {
    private static Logger log = LoggerFactory.getLogger(BoardRepositoryTest.class);

    @Autowired
    public BoardRepository boardRepository;

    @Disabled("다른 테스트와 간섭")
    @Test
    public void selectBoards() {
        List<BoardDTO> boards = boardRepository.selectBoardsWithFileCheck();

        log.info(boards.toString());
        assertThat(boards).hasSize(10);
    }

    @Test
    public void selectBoard() {
        Long boardIdx = 5L;
        BoardDTO boardDTO = boardRepository.selectBoard(boardIdx);

        log.info(boardDTO.toString());
        assertThat(boardDTO.getTitle()).isEqualTo("Title 5");
        assertThat(boardDTO.getWriter()).isEqualTo("테5");
    }

    @Test
    public void selectBoardWithDetails() {
        Long boardIdx = 1L;
        BoardDTO boardDTO = boardRepository.selectBoardWithDetails(boardIdx);

        log.info(boardDTO.toString());
        assertThat(boardDTO.getBoardIdx()).isEqualTo(1);
        assertThat(boardDTO.getComments()).hasSize(1);
        assertThat(boardDTO.getFiles()).hasSize(1);
    }

    @Test
    public void selectBoardWithFiles() {
        Long boardIdx = 10L;
        BoardDTO boardDTO = boardRepository.selectBoardWithFiles(boardIdx);

        log.info(boardDTO.toString());
        assertThat(boardDTO.getBoardIdx()).isEqualTo(10);
        assertThat(boardDTO.getComments()).isNull();
        assertThat(boardDTO.getFiles()).hasSize(1);
    }

    @Test
    public void increaseHit() {
        Long boardIdx = 1L;
        int affectedRows = boardRepository.increaseHit(boardIdx);

        assertThat(affectedRows).isEqualTo(1);

        BoardDTO boardDTO = boardRepository.selectBoardWithDetails(boardIdx);

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

        boardRepository.insertBoard(board);
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

        boardRepository.insertFile(file);
        log.debug(file.toString());

        assertThat(file.getFileIdx()).isEqualTo(11);
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

        int affectedRows = boardRepository.updateBoard(board);
        assertThat(affectedRows).isEqualTo(1);

        BoardDTO selectBoard = boardRepository.selectBoard(1L);
        assertThat(selectBoard.getTitle()).isEqualTo("제목 999로 수정");
    }

    @Disabled("위쪽 테스트와 간섭")
    @Test
    public void selectFileIndexes() {
        List<Long> fileIndexes = boardRepository.selectFileIndexes(1L);
        assertThat(fileIndexes).hasSize(1);
    }

    @Test
    public void selectSavedFileName() {
        String fileName = boardRepository.selectSavedFileName(1L);
        assertThat(fileName).isEqualTo("file1.png");
    }

    @Test
    public void deleteFile() {
        int affectedRows = boardRepository.deleteFile(1L);
        assertThat(affectedRows).isEqualTo(1);
        assertThat(boardRepository.selectSavedFileName(1L)).isNull();
    }

    @Test
    public void insertComment() {
        CommentDTO comment = CommentDTO.builder()
                .writer("테스터")
                .password("rkskekfka1!")
                .content("내용 1")
                .boardIdx(1)
                .build();

        boardRepository.insertComment(comment);

        assertThat(comment.getCommentIdx()).isEqualTo(11);
    }

    @Test
    public void selectComment() {
        CommentDTO comment = boardRepository.selectComment(5L);

        assertThat(comment.getContent()).isEqualTo("Comment 5");
    }

    @Test
    public void deleteComment() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentIdx(1L);
        commentDTO.setPassword("commentpass1!");
        int affectedRows = boardRepository.deleteComment(commentDTO);

        assertThat(affectedRows).isEqualTo(1);
        assertThat(boardRepository.deleteComment(commentDTO)).isZero();
    }

    @Test
    public void selectAllCategory() {
        List<CategoryDTO> categories = boardRepository.selectAllCategory();

        assertThat(categories).hasSize(10);
    }
}