package com.study.ebsoft.controller;

import com.study.ebsoft.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/categories")
    public ResponseEntity<Object> findAllCategories(Map<String, Object> response) {
        log.debug("findAllCategories 호출");
        response.put("categories", categoryService.findAll());
        return new ResponseEntity<>(response,  HttpStatus.OK);
    }
}
