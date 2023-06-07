package com.study.ebsoft.mapper;

import com.study.ebsoft.domain.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();
}
