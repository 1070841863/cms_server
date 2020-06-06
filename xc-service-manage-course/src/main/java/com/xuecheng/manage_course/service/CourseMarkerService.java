package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.interceptor.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.smartcardio.CommandAPDU;
import java.util.Optional;

/**
 * @author study
 * @create 2020-04-08 17:29
 */
@Service
public class CourseMarkerService {

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    public CourseMarket getcourseMarketInfo(String id) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(id);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return optional.get();
    }

    public ResponseResult addOrUpdateInfo(CourseMarket courseMarket){
        //首先判断该课程id在课程营销表里面是否已经存在，如果存在的话，代表是修改操作。
        //否则是增加操作
//        String id = courseMarket.getId();
        //查询该课程营销是否已经存在
//        Optional<CourseMarket> optional = courseMarketRepository.findById(id);
//        if(!optional.isPresent()){
            //代表不存在
            //执行增加
//            courseMarketRepository.save(courseMarket);
//        }
//        else{
            courseMarketRepository.save(courseMarket);
//        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
