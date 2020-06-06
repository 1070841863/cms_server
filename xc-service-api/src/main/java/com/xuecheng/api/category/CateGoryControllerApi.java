package com.xuecheng.api.category;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author study
 * @create 2020-04-18 16:14
 */
@Api("获取分类")
public interface CateGoryControllerApi {
    @ApiOperation("获取分类")
    public CategoryNode getCateGory();
}
