package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePicControllerApi;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CoursePicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author study
 * @create 2020-04-09 17:30
 */
@RestController
@RequestMapping("/course")
public class CoursePicController implements CoursePicControllerApi {

    @Autowired
    private CoursePicService coursePicService;

    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(String courseId, String pic){
        return coursePicService.addCoursePic(courseId,pic);
    }

    @PreAuthorize("hasAuthority('course_pic_list')")
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic getPicByCourseId(@PathVariable("courseId") String courseId) {
        return coursePicService.getPicByCourseId(courseId);
    }

    @DeleteMapping("/coursepic/delete")
    public ResponseResult deletePicByCourseId(String courseId) {
        return coursePicService.deletePicByCourseId(courseId);
    }

}
