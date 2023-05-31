package com.study.ebsoft.repository.board;

import com.study.ebsoft.dto.BoardDTO;
import com.study.ebsoft.model.board.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Board repository 접근
 */
@Slf4j
@Repository
public class BoardDAO {

    private static final String FIND_BY_ID = "SELECT b.board_idx, c.category_idx, c.category, b.title, b.writer, b.content, b.password, b.hit, b.regdate, b.moddate FROM tb_board b JOIN tb_category c ON b.category_idx = c.category_idx WHERE b.board_idx = ?";
    private static final String FIND_ALL = "SELECT b.board_idx, c.category, b.title, b.writer, b.content, b.password, b.hit, b.regdate, b.moddate,\n" +
            "(CASE WHEN EXISTS (SELECT 1 FROM tb_file f WHERE f.board_idx = b.board_idx) THEN 1 ELSE 0 END) AS has_file\n" +
            "FROM tb_board b\n" +
            "JOIN tb_category c ON b.category_idx = c.category_idx\n" +
            "LEFT OUTER JOIN tb_file f ON b.board_idx = f.board_idx\n";
    private static final String SAVE = "INSERT INTO tb_board (category_idx, title, writer, content, password, hit, regdate) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE tb_board SET title = ?, writer = ?, content = ?, moddate = ? WHERE board_idx = ? and password = ?";
    private static final String INCREASE_HIT = "UPDATE tb_board SET hit = hit + 1 WHERE board_idx = ?";
    private static final String DELETE = "DELETE FROM tb_board WHERE board_idx = ? and password = ?";

    public void driverFind() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public BoardDTO findById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        BoardDTO boardDTO = null;
        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boardDTO = new BoardDTO();
                boardDTO.setBoardIdx(resultSet.getLong("b.board_idx"));
                boardDTO.setCategoryIdx(resultSet.getInt("c.category_idx"));
                boardDTO.setCategory(resultSet.getString("c.category"));
                boardDTO.setTitle(resultSet.getString("b.title"));
                boardDTO.setWriter(resultSet.getString("b.writer"));
                boardDTO.setContent(resultSet.getString("b.content"));
                boardDTO.setPassword((resultSet.getString("b.password")));
                boardDTO.setHit(resultSet.getInt("b.hit"));
                boardDTO.setRegDate(resultSet.getTimestamp("b.regdate").toLocalDateTime());
                if (resultSet.getTimestamp("b.moddate") != null) {
                    boardDTO.setModDate(resultSet.getTimestamp("b.moddate").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return boardDTO;
    }

    public List<BoardDTO> findAllWithFileCheck(String queryBuilder) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(queryConditionSetting(FIND_ALL, queryBuilder));
            log.debug("Dynamic Query : {} ",  queryConditionSetting(FIND_ALL, queryBuilder));
            resultSet = statement.executeQuery();
            List<BoardDTO> boards = new ArrayList<>();
            while (resultSet.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setBoardIdx(resultSet.getLong("b.board_idx"));
                boardDTO.setCategory(resultSet.getString("c.category"));
                boardDTO.setTitle(resultSet.getString("b.title"));
                boardDTO.setWriter(resultSet.getString("b.writer"));
                boardDTO.setContent(resultSet.getString("b.content"));
                boardDTO.setPassword((resultSet.getString("b.password")));
                boardDTO.setHit(resultSet.getInt("b.hit"));
                boardDTO.setRegDate(resultSet.getTimestamp("b.regdate").toLocalDateTime());
                if(resultSet.getTimestamp("b.moddate") != null ) {
                    boardDTO.setModDate(resultSet.getTimestamp("b.moddate").toLocalDateTime());
                }
                boolean hasImage = resultSet.getInt("has_file") == 1;
                boardDTO.setHasFile(hasImage);
                boards.add(boardDTO);
            }
            return boards;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String queryConditionSetting(String findAll, String queryBuilder) {
        if( queryBuilder != null) {
            return FIND_ALL + queryBuilder;
        }
        return findAll;
    }

    public BoardDTO save(Board board) {
        driverFind();
        log.debug(" save() 메서드 호출 -> board : {}", board.toString());
        Connection connection = null;
        PreparedStatement statement = null;

        BoardDTO boardDTO = new BoardDTO();
        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(SAVE,Statement.RETURN_GENERATED_KEYS );
            statement.setInt(1, board.getCategoryIdx().getCategoryIdx());
            statement.setString(2, board.getTitle().getTitle());
            statement.setString(3, board.getWriter().getWriter());
            statement.setString(4, board.getContent().getContent());
            statement.setString(5, board.getPassword().getPassword());
            statement.setInt(6, board.getHit().getHit());
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                boardDTO.setBoardIdx(generatedKeys.getLong(1));
            }
            log.debug(" save() 메서드 종료 -> BoardIdx : {}", boardDTO.getBoardIdx());
            return boardDTO;
        } catch (SQLException e) {
            e.printStackTrace();
            return boardDTO;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public BoardDTO update(Board board) {
        Connection connection = null;
        PreparedStatement statement = null;

        BoardDTO boardDTO = null;
        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(UPDATE);
            statement.setString(1, board.getTitle().getTitle());
            statement.setString(2, board.getWriter().getWriter());
            statement.setString(3, board.getContent().getContent());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(5, board.getBoardIdx().getBoardIdx());
            statement.setString(6, board.getPassword().getPassword());
            log.debug("BoardDAO > update() > BoardIDX : {}",board.getBoardIdx().getBoardIdx());
            log.debug("BoardDAO > update() > Password : {}",board.getPassword().getPassword());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 1) {
                boardDTO = new BoardDTO();
                boardDTO.setBoardIdx(board.getBoardIdx().getBoardIdx());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return boardDTO;
    }


    public BoardDTO increaseHitCount(long boardIdx) {
        Connection connection = null;
        PreparedStatement statement = null;

        BoardDTO boardDTO = new BoardDTO();
        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(INCREASE_HIT);
            statement.setLong(1, boardIdx);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }
            boardDTO.setBoardIdx(boardIdx);
            return boardDTO;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteById(Long id, String password) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.setString(2, password);

            statement.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
