package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author study
 * @create 2020-04-09 17:30
 */
@Api(value = "课程图片管理",description = "课程图片管理，crud")
public interface CoursePicControllerApi {
    //增加课程id下的图片
    @ApiOperation("增加课程id下的图片")
    public ResponseResult addCoursePic(String courseId,String pic);

    //根据课程id查询图片
    @ApiOperation("根据课程id查询图片")
    public CoursePic getPicByCourseId(String courseId);

    @ApiOperation("删除图片")
    public ResponseResult deletePicByCourseId(String courseId);
}
