package com.study.ebsoft.repository;

import com.study.ebsoft.domain.Category;
import com.study.ebsoft.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    public CategoryMapper categoryMapper;

    @Autowired
    public CategoryRepository(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
}
