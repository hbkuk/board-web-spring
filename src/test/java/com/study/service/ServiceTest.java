package com.study.service;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.repository.BoardRepository;
import com.study.ebsoft.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        boardService = new BoardService(boardRepository);
    }

    @Nested
    @DisplayName("게시글이 가져올때")
    class getBoard {

        @Test
        @DisplayName("조회수가 증가되지 안았다면 예외가 발생한다")
        void views_not_increased_exception() {

            when(boardRepository.increaseHit(1L)).thenReturn(0);

            assertThatExceptionOfType(NoSuchElementException.class)
                    .isThrownBy(() -> {
                        boardService.selectBoardWithDetails(1L);
                    })
                    .withMessageMatching("해당 글을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("조회수 증가, 게시글 찾기, 모든 댓글 찾기, 모든 파일 찾기의 메서드가 순서대로 호출된다.")
        void board_click_triggers_method_calls() {
            // given
            Long boardIdx = 1L;

            BoardDTO boardDTO = new BoardDTO();
            when(boardRepository.increaseHit(boardIdx)).thenReturn(1);

            // when
            boardService.selectBoardWithDetails(boardIdx);

            // then
            InOrder inOrder = inOrder(boardRepository);
            inOrder.verify(boardRepository).increaseHit(boardIdx);
            inOrder.verify(boardRepository).findBoardAndCommentAndFile(boardIdx);
            inOrder.verifyNoMoreInteractions();
        }

    }

    @Nested
    @DisplayName("게시글을 저장할때")
    class writeBoard {

        @Test
        @DisplayName("업로드된 파일이 없다면 글만 저장되어야 한다.")
        void save_only_post_when_no_uploaded_file() {
            // given
            long expectedBoardIdx = 1L;

            BoardDTO board = BoardDTO.builder()
                    .categoryIdx(1)
                    .title("Title 1")
                    .writer("테스터")
                    .content("Content 1")
                    .password("rkskekfkakqkt!1")
                    .build();

            FileDTO file = FileDTO.builder()
                    .originalFileName("test.png")
                    .savedFileName("test.png")
                    .fileSize(127904)
                    .boardIdx(expectedBoardIdx)
                    .build();

            List<FileDTO> files = new ArrayList<>();
            board.setFiles(files);

            // when
            boardService.insertBoardWithFiles(board);

            // then
            verify(boardRepository, times(1)).insertBoard(board);
            verify(boardRepository, never()).insertFile(file);
        }

        @Test
        @DisplayName("업로드된 파일이 있으면 글과 파일이 저장되어야 한다.")
        void save_post_and_file_when_exist_uploaded_file() {
            // given
            long expectedBoardIdx = 1L;

            BoardDTO board = BoardDTO.builder()
                    .categoryIdx(1)
                    .title("Title 1")
                    .writer("테스터")
                    .content("Content 1")
                    .password("rkskekfkakqkt!1")
                    .build();

            FileDTO file = FileDTO.builder()
                    .originalFileName("test.png")
                    .savedFileName("test.png")
                    .fileSize(127904)
                    .boardIdx(expectedBoardIdx)
                    .build();

            List<FileDTO> files = new ArrayList<>();
            files.add(file);

            board.setFiles(files);

            // when
            boardService.insertBoardWithFiles(board);

            // then
            verify(boardRepository, times(1)).insertBoard(board);
            verify(boardRepository, times(1)).insertFile(file);
        }

    }

    @Nested
    @DisplayName("게시글이 수정 시")
    class modifyBoard {

        @Nested
        @DisplayName("게시글을 가져올때")
        class getBoard {

            @Test
            @DisplayName("게시글 수정을 눌렀을때 메서드가 다음과 같은 순서대로 호출된다.")
            void modify_post_click_triggers_method_calls() {
                // given
                Long boardIdx = 1L;
                BoardDTO boardDTO = new BoardDTO();
                when(boardRepository.findBoardFile(boardIdx)).thenReturn(boardDTO);

                // when
                boardService.selectBoardWithFiles(boardIdx);

                // then
                verify(boardRepository, times(1)).findBoardFile(boardIdx);
            }

            @Test
            @DisplayName("게시글 수정을 눌렀을때 글을 찾지 못했다면 예외가 발생한다.")
            void modify_post_not_found_exception() {

                when(boardRepository.findBoardFile(1L)).thenReturn(null);

                assertThatExceptionOfType(NoSuchElementException.class)
                        .isThrownBy(() -> {
                            boardService.selectBoardWithFiles(1L);
                        })
                        .withMessageMatching("해당 글을 찾을 수 없습니다.");
            }
        }

        @Nested
        @DisplayName("기존의 업로드된 파일이")
        class modifyUploadFiles {

            @Test
            @DisplayName("없다면, 게시글의 내용만 수정할 경우 게시글만 수정된다.")
            void before_notting_uploaded_files_modify_post_when_only_text_update() {
                // given
                Long boardIdx = 1L;

                BoardDTO updateBoard = BoardDTO.builder()
                        .boardIdx(boardIdx)
                        .categoryIdx(1)
                        .title("Title 1")
                        .writer("테스터")
                        .content("Content 1")
                        .password("rkskekfkakqkt!1")
                        .build();

                BoardDTO selectBoardDTO = new BoardDTO();

                BoardDTO updateReturnBoardDTO = new BoardDTO();
                updateReturnBoardDTO.setBoardIdx(boardIdx);

                List<FileDTO> newUploadFiles = new ArrayList<>();
                updateBoard.setFiles(newUploadFiles);
                List<Long> previouslyUploadedIndexes = new ArrayList<>();
                List<Long> getDbFileIndexes = new ArrayList<>();

                when(boardRepository.findBoard(boardIdx)).thenReturn(selectBoardDTO);
                when(boardRepository.updateBoard(updateBoard)).thenReturn(1);
                when(boardRepository.findFileIndexes(boardIdx)).thenReturn(getDbFileIndexes);

                // when
                boardService.updateBoardWithFiles(updateBoard, previouslyUploadedIndexes);

                // then
                verify(boardRepository, times(1)).updateBoard(updateBoard);
            }

            @Test
            @DisplayName("3개일때 , 게시글 수정시 내용만 수정할 경우 게시글만 수정하고 기존 파일은 유지된다.")
            void before_uploaded_files_modify_post_when_only_text_update() {
                // given
                Long boardIdx = 1L;

                BoardDTO updateBoard = BoardDTO.builder()
                        .boardIdx(boardIdx)
                        .categoryIdx(1)
                        .title("Title 1")
                        .writer("테스터")
                        .content("Content 1")
                        .password("rkskekfkakqkt!1")
                        .build();

                BoardDTO selectBoardDTO = new BoardDTO();

                BoardDTO updateReturnBoardDTO = new BoardDTO();
                updateReturnBoardDTO.setBoardIdx(boardIdx);

                List<FileDTO> newUploadFiles = new ArrayList<>();
                updateBoard.setFiles(newUploadFiles);

                List<Long> previouslyUploadedIndexes = Arrays.asList(10L, 11L, 12L);

                List<Long> dbFileIndexes = Arrays.asList(10L, 11L, 12L);

                when(boardRepository.findBoard(boardIdx)).thenReturn(selectBoardDTO);
                when(boardRepository.updateBoard(updateBoard)).thenReturn(1);
                when(boardRepository.findFileIndexes(boardIdx)).thenReturn(dbFileIndexes);

                // when
                boardService.updateBoardWithFiles(updateBoard, previouslyUploadedIndexes);

                // then
                verify(boardRepository, times(1)).updateBoard(updateBoard);

                dbFileIndexes.stream()
                        .forEach(fileIdx ->
                                verify(boardRepository, never()).deleteFile(fileIdx));
            }

            @Test
            @DisplayName("3개일때, 3개의 파일을 삭제 시 게시글은 수정하고 DB에 저장된 파일정보는 삭제된다.")
            void before_uploaded_files_modify_post_when_delete_all_uploaded_files() {
                // given
                Long boardIdx = 1L;

                BoardDTO updateBoard = BoardDTO.builder()
                        .boardIdx(boardIdx)
                        .categoryIdx(1)
                        .title("Title 1")
                        .writer("테스터")
                        .content("Content 1")
                        .password("rkskekfkakqkt!1")
                        .build();

                BoardDTO selectBoardDTO = new BoardDTO();

                BoardDTO updateReturnBoardDTO = new BoardDTO();
                updateReturnBoardDTO.setBoardIdx(boardIdx);

                List<FileDTO> newUploadFiles = new ArrayList<>();
                updateBoard.setFiles(newUploadFiles);

                List<Long> previouslyUploadedIndexes = new ArrayList<>();

                List<Long> dbFileIndexes = Arrays.asList(10L, 11L, 12L);

                List<Long> fileIndexesToDelete = new ArrayList<>(dbFileIndexes);
                fileIndexesToDelete.removeAll(previouslyUploadedIndexes);

                when(boardRepository.findBoard(boardIdx)).thenReturn(selectBoardDTO);
                when(boardRepository.updateBoard(updateBoard)).thenReturn(1);
                when(boardRepository.findFileIndexes(boardIdx)).thenReturn(dbFileIndexes);

                when(boardRepository.findSavedFileName(10L)).thenReturn("image1.jpg");
                when(boardRepository.findSavedFileName(11L)).thenReturn("image2.jpg");
                when(boardRepository.findSavedFileName(12L)).thenReturn("image3.jpg");

                // when
                boardService.updateBoardWithFiles(updateBoard, previouslyUploadedIndexes);

                // then
                verify(boardRepository, times(1)).updateBoard(updateBoard);

                fileIndexesToDelete.stream()
                        .forEach(fileIdx -> verify(boardRepository, times(1)).deleteFile(fileIdx));
            }

            @Test
            @DisplayName("2개의 일때, 1개의 파일을 추가로 업로드할 경우 게시글은 수정하고 추가로 업로드한다.")
            void before_uploaded_files_modify_post_when_new_file() {
                // given
                Long boardIdx = 1L;

                BoardDTO updateBoard = BoardDTO.builder()
                        .boardIdx(boardIdx)
                        .categoryIdx(1)
                        .title("Title 1")
                        .writer("테스터")
                        .content("Content 1")
                        .password("rkskekfkakqkt!1")
                        .build();

                BoardDTO selectBoardDTO = new BoardDTO();

                BoardDTO updateReturnBoardDTO = new BoardDTO();
                updateReturnBoardDTO.setBoardIdx(boardIdx);

                FileDTO newFile = FileDTO.builder()
                        .originalFileName("test1.png")
                        .savedFileName("test1.png")
                        .fileSize(127904)
                        .build();

                List<FileDTO> newUploadFiles = Arrays.asList(newFile);
                updateBoard.setFiles(newUploadFiles);

                List<Long> previouslyUploadedIndexes = Arrays.asList(10L, 11L);

                List<Long> dbFileIndexes = Arrays.asList(10L, 11L);

                when(boardRepository.findBoard(boardIdx)).thenReturn(selectBoardDTO);
                when(boardRepository.updateBoard(updateBoard)).thenReturn(1);
                when(boardRepository.findFileIndexes(boardIdx)).thenReturn(dbFileIndexes);

                // when
                boardService.updateBoardWithFiles(updateBoard, previouslyUploadedIndexes);

                // then
                verify(boardRepository, times(1)).updateBoard(updateBoard);
                verify(boardRepository, times(1)).insertFile(newUploadFiles.get(0));
                dbFileIndexes.stream()
                        .forEach(fileIdx ->
                                verify(boardRepository, never()).deleteFile(fileIdx));
            }
        }

    }

    @Nested
    @DisplayName("게시글 삭제할때")
    class deleteBoard {

        @DisplayName("비밀번호가 같을 경우 게시글과, 댓글, 파일이 삭제된다.")
        @Test
        void all_delete_when_equal_password() {
            // given
            Long boardIdx = 1L;

            BoardDTO deleteBoardDTO = new BoardDTO();
            deleteBoardDTO.setBoardIdx(boardIdx);
            deleteBoardDTO.setPassword("qudrnr132!");

            BoardDTO actureBoardDTO = new BoardDTO();
            actureBoardDTO.setBoardIdx(boardIdx);
            actureBoardDTO.setPassword("qudrnr132!");

            List<Long> dbFileIndexes = Arrays.asList(10L, 11L, 12L);

            when(boardRepository.findBoard(boardIdx)).thenReturn(actureBoardDTO);
            when(boardRepository.findFileIndexes(deleteBoardDTO.getBoardIdx())).thenReturn(dbFileIndexes);
            when(boardRepository.findSavedFileName(10L)).thenReturn("image1.jpg");
            when(boardRepository.findSavedFileName(11L)).thenReturn("image2.jpg");
            when(boardRepository.findSavedFileName(12L)).thenReturn("image3.jpg");

            // when
            boardService.deleteBoardWithFilesAndComment(deleteBoardDTO);

            // then
            verify(boardRepository, times(1)).deleteBoard(deleteBoardDTO);
            verify(boardRepository, atLeastOnce()).deleteAllComment(deleteBoardDTO.getBoardIdx());
            dbFileIndexes.stream()
                    .forEach(fileIdx -> verify(boardRepository, times(1)).deleteFile(fileIdx));

        }

        @DisplayName("비밀번호가 다를 경우 예외가 발생한다")
        @Test
        void throw_exception_when_not_equal_password() {
            // given
            Long boardIdx = 1L;

            BoardDTO deleteBoardDTO = new BoardDTO();
            deleteBoardDTO.setBoardIdx(boardIdx);
            deleteBoardDTO.setPassword("qudrnr132");

            BoardDTO actureBoardDTO = new BoardDTO();
            actureBoardDTO.setBoardIdx(boardIdx);
            actureBoardDTO.setPassword("qudrnr132!");

            when(boardRepository.findBoard(boardIdx)).thenReturn(actureBoardDTO);

            // then
            assertThatIllegalArgumentException()
                    .isThrownBy(() ->
                    {
                        boardService.deleteBoardWithFilesAndComment(deleteBoardDTO);
                    })
                    .withMessageMatching("비밀번호가 다릅니다.");
        }
    }
}


