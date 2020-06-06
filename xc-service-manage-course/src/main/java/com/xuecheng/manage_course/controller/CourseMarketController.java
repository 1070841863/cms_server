package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseMarketControllerApi;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseMarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author study
 * @create 2020-04-08 17:28
 */
@RestController
@RequestMapping("/courseMarket")
public class CourseMarketController implements CourseMarketControllerApi {

    @Autowired
    private CourseMarkerService courseMarkerService;

    @GetMapping("/list/{id}")
    public CourseMarket getcourseMarketInfo(@PathVariable("id") String id) {
        return courseMarkerService.getcourseMarketInfo(id);
    }

    @PostMapping("/add")
    public ResponseResult addOrUpdateInfo(@RequestBody CourseMarket courseMarket) {
        return courseMarkerService.addOrUpdateInfo(courseMarket);
    }

}
