package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author study
 * @create 2020-04-16 17:39
 */
@RestController
@RequestMapping("/search")
public class SearchController implements EsControllerApi {

    @Autowired
    private SearchService searchService;

    @GetMapping("/course/info/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page,@PathVariable("size") int size,CourseSearchParam courseSearchParam) {
        return searchService.list(page,size,courseSearchParam);
    }

    @GetMapping("/getall/{id}")
    public Map<String, CoursePub> getall(@PathVariable("id") String id) {
        return searchService.getall(id);
    }

    @GetMapping("/getmedia/{teachplanId}")
    public TeachplanMediaPub getallMediaPub(@PathVariable("teachplanId") String teachplanId) {
        String[] strings=new String[]{teachplanId};
        QueryResponseResult<TeachplanMediaPub> res = searchService.getallMediaPub(strings);
        QueryResult<TeachplanMediaPub> queryResult = res.getQueryResult();
        if(queryResult!=null){
            List<TeachplanMediaPub> list = queryResult.getList();
            if(list!=null&&list.size()>0){
                return list.get(0);
            }
        }
        return new TeachplanMediaPub();
    }

}
