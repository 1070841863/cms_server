package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author study
 * @create 2020-04-07 16:51
 */
@Api(value = "课程管理接口",description = "课程管理接口，提供课程的curd")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("课程我的信息信息列表")
    public QueryResponseResult findCourseList(Integer page, Integer size,CourseListRequest courseListRequest);

    @ApiOperation("课程分类信息查询")
    public CategoryNode findCategoryNodeList();

    @ApiOperation("课程信息添加")
    public ResponseResult addCourse(CourseBase courseBase);

    @ApiOperation("根据id查询课程信息")
    public CourseBase getOneCourse(String id);

    @ApiOperation("修改课程信息")
    public ResponseResult updateCourse(String id,CourseBase courseBase);

    @ApiOperation("课程视图查询")
    public CourseView courseview(String courseid);

    @ApiOperation("预览课程")
    public CoursePublishResult preview(String id);

    @ApiOperation("发布课程")
    public CoursePublishResult publish(String id);

    @ApiOperation("保存课程计划与媒资文件的关联")
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia);

}
