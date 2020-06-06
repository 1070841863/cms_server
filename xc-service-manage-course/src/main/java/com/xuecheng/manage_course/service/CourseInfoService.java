package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.interceptor.ExceptionCast;
import com.xuecheng.framework.interceptor.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CategoryMapper;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author study
 * @create 2020-04-08 13:28
 */
@Service
public class CourseInfoService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CourseBaseRepository courseBaseRepository;

    //查询课程信息
    public QueryResponseResult findCourseList(Integer page, Integer size,String companyId,CourseListRequest courseListRequest){
        if(courseListRequest==null){
            courseListRequest=new CourseListRequest();
        }
        courseListRequest.setCompanyId(companyId);
        PageHelper.startPage(page,size);
        Page<CourseInfo> allCourseList = courseMapper.findAllCourseList(courseListRequest);
        List<CourseInfo> result = allCourseList.getResult();
        QueryResult queryResult=new QueryResult();
        queryResult.setTotal(allCourseList.getTotal());//总记录数
        queryResult.setList(result);//数据列表
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    public CategoryNode findCategoryNodeList(){
        return categoryMapper.getAll();
    }

    public ResponseResult addCourse(CourseBase courseBase){
        CourseBase courseBase1 = courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    public CourseBase getOneCourse(String id){
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if(!optional.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_NO_RESULT);
        }
        CourseBase courseBase = optional.get();
        return courseBase;
    }

    public ResponseResult updateCourse(String id,CourseBase courseBase){
        courseBase.setId(id);
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
