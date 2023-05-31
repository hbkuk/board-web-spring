package com.study.ebsoft.repository.category;

import com.study.ebsoft.dto.CategoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class CategoryDAO {
    private static final String FIND_ALL = "SELECT * FROM tb_category";

    public void driverFind() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<CategoryDTO> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<CategoryDTO> categorys = new ArrayList<>();
        try {
            driverFind();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(FIND_ALL);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setCategoryIdx(resultSet.getInt("category_idx"));
                categoryDTO.setCategory(resultSet.getString("category"));
                categorys.add(categoryDTO);
            }
            return categorys;
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
        return categorys;
    }
}