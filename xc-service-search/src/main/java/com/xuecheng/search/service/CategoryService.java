package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author study
 * @create 2020-04-18 16:18
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    public CategoryNode getAll() {
        CategoryNode all = categoryMapper.getAll();
        return all;
    }
}
