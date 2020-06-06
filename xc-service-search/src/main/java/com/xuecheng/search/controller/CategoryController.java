package com.xuecheng.search.controller;

import com.xuecheng.api.category.CateGoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author study
 * @create 2020-04-18 16:16
 */
@RestController("category")
public class CategoryController implements CateGoryControllerApi {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/info")
    public CategoryNode getCateGory() {
        return categoryService.getAll();
    }
}
