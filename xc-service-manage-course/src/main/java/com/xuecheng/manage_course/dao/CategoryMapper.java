package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author study
 * @create 2020-04-08 14:35
 */
@Mapper
public interface CategoryMapper {

    public CategoryNode getAll();
}
