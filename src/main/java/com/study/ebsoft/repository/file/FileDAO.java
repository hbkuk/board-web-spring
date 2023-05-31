package com.study.ebsoft.repository.file;

import com.study.ebsoft.dto.FileDTO;
import com.study.ebsoft.model.file.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class FileDAO {
    private static final String FIND_SAVED_FILE_NAME = "SELECT saved_name, original_name  FROM tb_file WHERE file_idx = ?";
    private static final String FIND_FILES = "SELECT * FROM tb_file WHERE board_idx = ?";
    private static final String FIND_FILE_INDEXS = "SELECT file_idx FROM tb_file WHERE board_idx = ?";
    private static final String SAVE = "INSERT INTO tb_file (saved_name, original_name, size, board_idx) VALUES (?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM tb_file WHERE file_idx = ?";

    public void driverFind() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public FileDTO findFileNameById(Long fileIdx) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        FileDTO fileDTO = null;

        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(FIND_SAVED_FILE_NAME);
            statement.setLong(1, fileIdx);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                log.debug("resultSet.getString(saved_name) : {}", resultSet.getString("saved_name"));
                log.debug("resultSet.getString(original_name) : {}", resultSet.getString("original_name"));
                fileDTO = new FileDTO();
                fileDTO.setSavedFileName(resultSet.getString("saved_name"));
                fileDTO.setOriginalFileName(resultSet.getString("original_name"));
                return fileDTO;
            }
            return null;
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
                // Handle exception
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<FileDTO> findFilesByBoardId(Long boardIdx) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(FIND_FILES);
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            List<FileDTO> files = new ArrayList<>();
            while (resultSet.next()) {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setFileIdx(resultSet.getLong("file_idx"));
                fileDTO.setSavedFileName(resultSet.getString("saved_name"));
                fileDTO.setOriginalFileName(resultSet.getString("original_name"));
                fileDTO.setFileSize(resultSet.getInt("size"));
                fileDTO.setBoardIdx(resultSet.getLong("board_idx"));
                files.add(fileDTO);
            }
            return files;
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

    public List<Long> findFileIndexesByBoardId(Long boardIdx) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(FIND_FILE_INDEXS);
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            List<Long> fileIndexes = new ArrayList<>();
            while (resultSet.next()) {
                fileIndexes.add(resultSet.getLong("file_idx"));
            }
            return fileIndexes;
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



    public FileDTO save(File file, long boardIdx){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        FileDTO fileDTO = null;
        log.debug("File Save -> Save File Name : {} ", file.getSavedFileName());

        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            preparedStatement = connection.prepareStatement(SAVE);
            preparedStatement.setString(1, file.getSavedFileName());
            preparedStatement.setString(2, file.getOriginalName().getFileName());
            preparedStatement.setInt(3, file.getFileSize().getImageSize());
            preparedStatement.setLong(4, boardIdx);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                log.debug("File Save 성공");
                fileDTO = new FileDTO();
                fileDTO.setBoardIdx(boardIdx);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return fileDTO;
    }

    public void deleteByFileId(Long image_idx) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(DELETE);
            statement.setLong(1, image_idx);
            statement.executeUpdate();
        } catch (SQLException e) {
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