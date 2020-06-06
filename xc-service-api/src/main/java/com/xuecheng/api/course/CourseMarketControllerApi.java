package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author study
 * @create 2020-04-08 17:23
 */
@Api(value = "课程营销信息")
public interface CourseMarketControllerApi {
    @ApiOperation("查询课程营销信息")
    public CourseMarket getcourseMarketInfo(String id);

    @ApiOperation("增加或修改营销信息")
    public ResponseResult addOrUpdateInfo(CourseMarket courseMarket);
}
