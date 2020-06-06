package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author study
 * @create 2020-04-09 17:34
 */
@Service
public class CoursePicService {

    @Autowired
    CoursePicRepository coursePicRepository;

    //增加课程id下的图片
    public ResponseResult addCoursePic(String courseId, String pic){
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic=null;
        if(optional.isPresent()){
            coursePic=optional.get();
        }
        if(coursePic==null){
            coursePic=new CoursePic();
        }

        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic getPicByCourseId(String courseId){
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    public ResponseResult deletePicByCourseId(String courseId){

        long flag = coursePicRepository.deleteByCourseid(courseId);
        //1为删除成功，0为删除失败
        if(flag>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
