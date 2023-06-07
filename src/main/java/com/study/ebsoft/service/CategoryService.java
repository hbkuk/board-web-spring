package com.study.ebsoft.service;

import com.study.ebsoft.domain.Board;
import com.study.ebsoft.domain.Category;
import com.study.ebsoft.repository.BoardRepository;
import com.study.ebsoft.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 카테고리를 리턴합니다
     *
     * @return 모든 카테고리
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
