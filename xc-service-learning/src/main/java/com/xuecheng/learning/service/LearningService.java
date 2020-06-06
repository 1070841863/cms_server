package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.interceptor.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author study
 * @create 2020-04-22 16:40
 */
@Service
public class LearningService {

    @Autowired
    private CourseSearchClient courseSearchClient;

    //获取课程的学习地址（播放地址）
    public GetMediaResult getmedia(String courseId, String teachplanId) {
        //校验学生的学习权限

        //远程调用搜索服务查询课程计划所在的课程媒资信息
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getallMediaPub(teachplanId);
        if(teachplanMediaPub==null|| StringUtils.isEmpty(teachplanMediaPub.getMediaUrl())){
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        return new GetMediaResult(CommonCode.SUCCESS,teachplanMediaPub.getMediaUrl());
    }
}
